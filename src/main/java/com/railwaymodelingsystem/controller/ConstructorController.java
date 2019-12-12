package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.model.AjaxResponseBody;
import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.service.StationService;
import com.railwaymodelingsystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

@Controller
public class ConstructorController {
    final
    StationService stationService;

    final
    UserService userService;

    public ConstructorController(StationService stationService, UserService userService) {
        this.stationService = stationService;
        this.userService = userService;
    }

    @GetMapping("/startConstructor/checkName")
    public ResponseEntity<AjaxResponseBody> checkStationNAme(@RequestParam(value = "name")String stationName, Principal principal){
        User user = userService.getByName(principal.getName());
        if(stationService.isExists(stationName, user))
            return ResponseEntity.ok(new AjaxResponseBody("Станция с именем " + stationName + "уже есть в базе данных", "ERROR"));
        else
            return ResponseEntity.ok(new AjaxResponseBody("Станции с именем " + stationName + "нет базе данных", "SUCCESS"));
    }

    @PostMapping("/startConstructor")
    public ResponseEntity<AjaxResponseBody> createStation(@ModelAttribute(name = "name") String name, Principal principal){
        Station station = new Station();
        User user = userService.getByName(principal.getName());

        station.setName(name);
       station.setUser(user);

       user.getStations().add(station);

        stationService.addStation(station);

        return ResponseEntity.ok(new AjaxResponseBody(name, "SUCCESS"));
    }

    @GetMapping("/constructor")
    public String startConstructor(@RequestParam(name = "station", required = false)String name, Model model)
    {
        if(name == null) {
        }
        else{
            model.addAttribute("stationName", name);
        }
        return "constructor";
    }
}
