package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.model.AjaxResponseBody;
import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.model.rms.*;
import com.railwaymodelingsystem.rms.Event;
import com.railwaymodelingsystem.rms.RMSException.ScheduleException;
import com.railwaymodelingsystem.rms.RMSException.SheduleException;
import com.railwaymodelingsystem.rms.RMSException.StationException;
import com.railwaymodelingsystem.rms.RMSException.TopologyException;
import com.railwaymodelingsystem.rms.Schedule;
import com.railwaymodelingsystem.rms.Scheduler;
import com.railwaymodelingsystem.service.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ModelingController {

    @AllArgsConstructor
    private static class ScheduleEntity implements Serializable {

        @Getter
        private List<Train> trains;

        @Getter
        private List<Event> events;

        private static class Train implements Serializable {

            @Getter
            private String number;

            @Getter
            private String length;

            @Getter
            private String type;

            @Getter
            private String cityFrom;

            @Getter
            private String cityTo;

            @Getter
            private String way;

            @Getter
            private String platform;

            @Getter
            private String arrive;

            @Getter
            private String departure;

            public Train(String number, String length, String type, String cityFrom, String cityTo, String way, String platform, String arrive, String departure) {
                this.number = number;
                this.length = length;
                this.type = type;
                this.cityFrom = cityFrom;
                this.cityTo = cityTo;
                this.way = way;
                this.platform = platform;
                this.arrive = arrive;
                this.departure = departure;
            }
        }

        private static class Event implements Serializable {

            @Getter
            private String block;

            @Getter
            private String type;

            @Getter
            private long time;

            public Event(String block, String type, long time) {
                this.block = block;
                this.type = type;
                this.time = time;
            }
        }
    }

    @Autowired
    private WayService wayService;

    @Autowired
    private UserService userService;

    @Autowired
    private SheduleService sheduleService;

    @Autowired
    private StationService stationService;

    @Autowired
    private LinkService linkService;

    @GetMapping("/modeling")
    public String startConstructor(Model model)
    {
        return "modeling";
    }

    @GetMapping("/modeling/schedule")
    @Transactional
    public ResponseEntity getSchedule(@RequestParam(value = "station") String stationName,
                                      Principal principal)
    {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(stationName, user);
        List<Way> ways = wayService.getByStation(station);
        List<Shedule> shedules = sheduleService.getShedulesByStation(station);
        Map<Block, List<Link>> blockLinksMap = new HashMap<>();
        for (Block block : station.getBlocks()) {
            blockLinksMap.put(block, linkService.getLinksByBlockFrom(block));
        }
        Schedule schedule = null;
        List<ScheduleEntity.Train> trains = new ArrayList<>();
        List<ScheduleEntity.Event> events = new ArrayList<>();
        try {
            Scheduler.buildStation(ways, shedules, blockLinksMap, station);
            schedule = Scheduler.getSchedule();
            schedule.printEvents();


            for (Shedule shedule : shedules) {
                trains.add(new ScheduleEntity.Train(shedule.getKey().getTrainNumber().toString(),
                        shedule.getTrainLength().toString(),
                        shedule.getTrainType().toString(),
                        shedule.getCityFrom().toString(),
                        shedule.getCityTo().toString(),
                        shedule.getWay().getNumber().toString(),
                        shedule.getPlatformNumber().toString(),
                        shedule.getArriveTime().toString(),
                        shedule.getDepartureTime().toString()
                ));
            }
            for (Event event : schedule.getEvents()) {
                events.add(new ScheduleEntity.Event(event.getBlock().toString(), event.getEventType().toString(), event.getTime()));
            }
        } catch (SheduleException | TopologyException | StationException | ScheduleException e) {
            ResponseEntity.ok(new AjaxResponseBody("Невозможно получить график движения\nПричина:\n" + e.getMessage(), "ERROR"));
        }
        return ResponseEntity.ok(new ScheduleEntity(trains, events));
    }
}
