package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.model.AjaxResponseBody;
import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.model.rms.*;
import com.railwaymodelingsystem.model.rms.compositeKey.LinkPrimary;
import com.railwaymodelingsystem.model.rms.compositeKey.ShedulePrimary;
import com.railwaymodelingsystem.service.*;
import com.railwaymodelingsystem.service.impl.SheduleServiceImpl;
import com.railwaymodelingsystem.service.impl.StationServiceImpl;
import com.railwaymodelingsystem.utils.StringValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Controller
public class SheduleController {

    @AllArgsConstructor
    private static class SheduleEntity{
        @Getter
        Integer trainNumber;

        @Getter
        Integer platform;

        @Getter
        Integer way;

        @Getter
        String type;

        @Getter
        Integer length;

        @Getter
        String arriveTime;

        @Getter
        String departureTime;

        @Getter
        String cityFrom;

        @Getter
        String cityTo;
    }

    @Autowired
    StationService stationService;

    @Autowired
    WayService wayService;

    @Autowired
    UserService userService;

    @Autowired
    TrainTypeService trainTypeService;

    @Autowired
    CityService cityService;

    @Autowired
    SheduleService sheduleService;

    @Autowired
    BlockService blockService;

    @GetMapping("/shedule")
    public String openShedule(@ModelAttribute(name = "station") String name, Model model, Principal principal){
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(name, user);
        List<Shedule> shedules = sheduleService.getShedulesByStation(station);
        List<Block> blockList = blockService.findAllByStation(station);
        List<TrainType> trainTypeList = trainTypeService.getAllTypes();
        List<City> cityList = cityService.getAllCity();

        ArrayList<Integer> platformList = new ArrayList<>();
        ArrayList<Way> wayList = new ArrayList<>();

        for (Block block : blockList) {
            Way addWay = block.getWay();
            Integer platform = block.getPlatformNumber();
            if(!wayList.contains(addWay))
                wayList.add(addWay);
            if(platform != null && !platformList.contains(platform))
                platformList.add(platform);
        }

        model.addAttribute("trainList", shedules);
        model.addAttribute("platformList", platformList);
        model.addAttribute("trainTypeList", trainTypeList);
        model.addAttribute("cityList", cityList);
        model.addAttribute("wayList", wayList);
        return "shedule";
    }

    @PostMapping(value = "/shedule/add_train", produces = "application/json")
    @Transactional
    public ResponseEntity addTrain(@RequestParam(value = "station")String stationName,
                                  @NotNull @ModelAttribute SheduleEntity shedule, Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(stationName, user);
        List<Way> wayList = wayService.getByWayAndStation(shedule.getWay(), station);
        TrainType trainType = trainTypeService.getTrainType(shedule.getType());
        Integer trainLength = shedule.getLength();

        Date arriveDate, departDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");

        City cityFrom = cityService.getCityByName(shedule.getCityFrom());
        City cityTo = cityService.getCityByName(shedule.getCityTo());
        try {
            arriveDate = dateFormat.parse(shedule.getArriveTime());
            departDate = dateFormat.parse(shedule.getDepartureTime());
        } catch (ParseException e) {
            return ResponseEntity.ok(new AjaxResponseBody("Некорректная даты прибытия", "ERROR"));
        }
        if (station == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
        } else if(shedule.getTrainNumber() < 1){
            return ResponseEntity.ok(new AjaxResponseBody("Номер поезда может быть только положительным", "ERROR"));
        } else if(shedule.getPlatform() < 1) {
            return ResponseEntity.ok(new AjaxResponseBody("Номер платформы может быть только положительным", "ERROR"));
        } else if(wayList.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Такого пути нет на станции", "ERROR"));
        } else if(trainType == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Недопустимый тип поезда", "ERROR"));
        } else if(trainLength < 0 || trainLength > 71) {
            return ResponseEntity.ok(new AjaxResponseBody("Длина поезда должна быть в диапазоне от 1 до 71 вагона", "ERROR"));
        } else if(cityFrom == null ||
                cityTo == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Некорректный маршрут следования", "ERROR"));
        }

        ShedulePrimary key = new ShedulePrimary();
        key.setTrainNumber(shedule.getTrainNumber());
        key.setStationId(station.getId());

        Shedule newShedule = new Shedule();
        newShedule.setStation(station);
        newShedule.setKey(key);
        newShedule.setPlatformNumber(shedule.getPlatform());
        newShedule.setWay(wayList.get(0));
        newShedule.setTrainType(trainType);
        newShedule.setTrainLength(trainLength);
        newShedule.setArriveTime(new Time(arriveDate.getTime()));
        newShedule.setDepartureTime(new Time(departDate.getTime()));
        newShedule.setCityFrom(cityFrom);
        newShedule.setCityTo(cityTo);

        sheduleService.addShedule(newShedule);

        return ResponseEntity.ok(new AjaxResponseBody("", "SUCCESS"));
    }

    @PostMapping(value = "/shedule/editTrain", produces = "application/json")
    @Transactional
    public ResponseEntity editTrain(@RequestParam(value = "station")String stationName,
                                    @RequestParam(value = "old_number")Integer oldNumber,
                                    @NotNull @ModelAttribute SheduleEntity shedule,
                                    Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(stationName, user);
        List<Way> wayList = wayService.getByWayAndStation(shedule.getWay(), station);
        TrainType trainType = trainTypeService.getTrainType(shedule.getType());
        Integer trainLength = shedule.getLength();

        Date arriveDate, departureDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");

        City cityFrom = cityService.getCityByName(shedule.getCityFrom());
        City cityTo = cityService.getCityByName(shedule.getCityTo());
        try {
            arriveDate = dateFormat.parse(shedule.getArriveTime());
            departureDate = dateFormat.parse(shedule.getDepartureTime());
        } catch (ParseException e) {
            return ResponseEntity.ok(new AjaxResponseBody("Некорректная дата прибытия или отправления", "ERROR"));
        }
        if (station == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
        }
        ShedulePrimary shedulePrimary = new ShedulePrimary();
        shedulePrimary.setStationId(station.getId());
        shedulePrimary.setTrainNumber(oldNumber);
        List<Shedule> sheduleList = sheduleService.getByKey(shedulePrimary);
        if (sheduleList.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Нет такого расписания", "ERROR"));
        } else if(shedule.getTrainNumber() < 1){
            return ResponseEntity.ok(new AjaxResponseBody("Номер поезда может быть только положительным", "ERROR"));
        } else if(shedule.getPlatform() < 1) {
            return ResponseEntity.ok(new AjaxResponseBody("Номер платформы может быть только положительным", "ERROR"));
        } else if(wayList.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Такого пути нет на станции", "ERROR"));
        } else if(trainType == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Недопустимый тип поезда", "ERROR"));
        } else if(trainLength < 0 || trainLength > 71) {
            return ResponseEntity.ok(new AjaxResponseBody("Длина поезда должна быть в диапазоне от 1 до 71 вагона", "ERROR"));
        } else if(cityFrom == null ||
                cityTo == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Некорректный маршрут следования", "ERROR"));
        }

        Shedule oldShedule = sheduleList.get(0);
        sheduleService.removeShedule(oldShedule.getKey());

        ShedulePrimary key = new ShedulePrimary();
        key.setTrainNumber(shedule.getTrainNumber());
        key.setStationId(station.getId());

        Shedule newShedule = new Shedule();
        newShedule.setKey(key);
        newShedule.getKey().setTrainNumber(shedule.getTrainNumber());
        newShedule.getKey().setStationId(station.getId());
        newShedule.setStation(station);
        newShedule.setPlatformNumber(shedule.getPlatform());
        newShedule.setWay(wayList.get(0));
        newShedule.setTrainType(trainType);
        newShedule.setTrainLength(trainLength);
        newShedule.setArriveTime(new Time(arriveDate.getTime()));
        newShedule.setDepartureTime(new Time(departureDate.getTime()));
        newShedule.setCityFrom(cityFrom);
        newShedule.setCityTo(cityTo);

        sheduleService.addShedule(newShedule);

        return ResponseEntity.ok(new AjaxResponseBody("", "SUCCESS"));
    }

    @GetMapping("/shedule/removeTrain")
    @Transactional
    public ResponseEntity removeTrain(@RequestParam(value = "trainNumber") Integer trainNumber,
                                      @RequestParam(value = "station") String stationName,
                                      Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(stationName, user);


        if (station == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
        } else if(trainNumber < 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Номер поезда должен быть положительным", "ERROR"));
        }

        ShedulePrimary shedulePrimary = new ShedulePrimary();
        shedulePrimary.setStationId(station.getId());
        shedulePrimary.setTrainNumber(trainNumber);
        List<Shedule> sheduleList = sheduleService.getByKey(shedulePrimary);

        if (sheduleList.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Нет такого расписания", "ERROR"));
        }

        sheduleService.removeShedule(sheduleList.get(0).getKey());

        return ResponseEntity.ok(new AjaxResponseBody("", "SUCCESS"));
    }
}
