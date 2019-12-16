package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.service.SheduleService;
import com.railwaymodelingsystem.service.StationService;
import com.railwaymodelingsystem.service.impl.SheduleServiceImpl;
import com.railwaymodelingsystem.service.impl.StationServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SheduleController {

    private final
    SheduleService sheduleService;

    private final
    StationService stationService;

    public SheduleController(SheduleServiceImpl sheduleService, StationServiceImpl stationService) {
        this.sheduleService = sheduleService;
        this.stationService = stationService;
    }

    @GetMapping("/start_shedule")
    public String startShedule() {
        return "startShedule";
    }

    @RequestMapping("/shedule")
    public String getShedule(Model model, @PathVariable(value = "station")String stationName){
        Station station = stationService.getStationByName(stationName);
        model.addAttribute("shedule", sheduleService.getShedulesByStation(station));
        return "shedule";
    }
}
