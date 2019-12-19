package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.model.rms.Shedule;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.service.SheduleService;
import com.railwaymodelingsystem.service.StationService;
import com.railwaymodelingsystem.service.UserService;
import com.railwaymodelingsystem.service.impl.SheduleServiceImpl;
import com.railwaymodelingsystem.service.impl.StationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Controller
public class SheduleController {
    @Autowired
    StationService stationService;

    @Autowired
    UserService userService;

    @GetMapping("/shedule")
    public String openShedule(@ModelAttribute(name = "name") String name, Model model, Principal principal){
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(name, user);
       ArrayList<Shedule> shedules = new ArrayList<>();
        try{
            shedules = new ArrayList<>(station.getShedules());
        }
        catch (NullPointerException ex){
            shedules = new ArrayList<>();
        }
        finally {
            model.addAttribute("trains", shedules);
            return "shedule";
        }
    }
}
