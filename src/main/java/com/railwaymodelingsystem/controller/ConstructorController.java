package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.model.AjaxResponseBody;
import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Link;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.model.rms.Way;
import com.railwaymodelingsystem.model.rms.compositeKey.LinkPrimary;
import com.railwaymodelingsystem.repository.LinkRepository;
import com.railwaymodelingsystem.service.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ConstructorController {

    @AllArgsConstructor
    private static class StationEntity implements Serializable {
        public static class Block implements Serializable {
            @Getter
            private String name;

            @Getter
            private Integer length;

            @Getter
            private Integer way;

            @Getter
            private Integer platformNumber;

            @Getter
            private String station;

            @Getter
            private List<String> upperBlocks = new ArrayList<>();

            public Block (String name, Integer length, Integer way, Integer platformNumber, String station) {
                this.name = name;
                this.length = length;
                this.way = way;
                this.platformNumber = platformNumber;
                this.station = station;
            }
        }
        private static class Way implements Serializable {
            @Getter
            private Integer number;

            @Getter
            private String station;

            public Way(Integer number, String station) {
                this.number = number;
                this.station = station;
            }
        }
        private static class Link implements Serializable {

            @Getter
            private String station;

            @Getter
            private String blockFrom;

            @Getter
            private String blockTo;

            public Link(String blockFrom, String blockTo, String station) {
                this.blockFrom = blockFrom;
                this.blockTo = blockTo;
                this.station = station;
            }
        }

        @Getter
        private String name;

        @Getter
        private List<Way> ways;

        @Getter
        private List<Block> blocks;

        @Getter
        private List<Link> links;
    }
    @Autowired
    CityService cityService;

    @Autowired
    WayService wayService;

    @Autowired
    BlockService blockService;

    @Autowired
    LinkRepository linkRepository;

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
    public ResponseEntity<AjaxResponseBody> createStation(@NotNull @ModelAttribute(name = "name") String name, @ModelAttribute(name = "city") String city, Principal principal){
        Station station = new Station();
        User user = userService.getByName(principal.getName());

        station.setName(name);
        station.setUser(user);
        station.setCity(cityService.getCityByName(city));
        user.getStations().add(station);

        stationService.addStation(station);
        return ResponseEntity.ok(new AjaxResponseBody(name, "SUCCESS"));
    }
    @PostMapping(value = "/constructor/removeStation", produces = "application/json")
    @Transactional
    public ResponseEntity removeStation(@NotNull @ModelAttribute(name = "name") String name, Principal principal) {
        stationService.deleteByNameAndUser(name, userService.getByName(principal.getName()));
        return ResponseEntity.ok(new AjaxResponseBody(name, "SUCCESS"));
    }

    @GetMapping("/constructor")
    public String startConstructor(Model model)
    {
        return "constructor";
    }

    @RequestMapping(value = "/constructor/station", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity getStationByName(@RequestParam("name") String name, Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(name, user);
        if(station == null)
            return ResponseEntity.ok(null);
        else {
            return ResponseEntity.ok(getStationEntity(station));
        }
    }

    @PostMapping(value = "/constructor/addWay", produces = "application/json")
    @Transactional
    public ResponseEntity addWay(@NotNull @ModelAttribute StationEntity.Way way, Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(way.getStation(), user);
        if (station == null) {
            //TODO станция не найдена
            return ResponseEntity.ok(null);
        } else if (wayService.getByWayAndStation(way.getNumber(), station) != null) {
            //TODO уже существует
            return ResponseEntity.ok(null);
        }
        Way newWay = new Way();
        newWay.setNumber(way.getNumber());
        newWay.setStation(station);
        wayService.addWay(newWay);
        return ResponseEntity.ok(getStationEntity(station));
    }

    @PostMapping(value = "/constructor/addBlock", produces = "application/json")
    @Transactional
    public ResponseEntity addBlock(@NotNull @ModelAttribute StationEntity.Block block, Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(block.getStation(), user);
        if (station == null) {
            //TODO станция не найдена
            return ResponseEntity.ok(null);
        } else if (blockService.findByNameAndStation(block.getName(), station) != null) {
            //TODO уже существует
            return ResponseEntity.ok(null);
        }
        Block newBlock = new Block();
        newBlock.setName(block.getName());
        newBlock.setLength(block.getLength());
        newBlock.setStation(station);
        if (block.getPlatformNumber() != null) {
            newBlock.setHasPlatform(true);
            newBlock.setPlatformNumber(block.getPlatformNumber());
        } else {
            newBlock.setHasPlatform(false);
        }
        Way way = wayService.getByWayAndStation(block.getWay(), station);
        if (way == null) {
            //TODO нет пути
            return ResponseEntity.ok(null);
        }
        newBlock.setWay(way);
        blockService.addBlock(newBlock);
        return ResponseEntity.ok(getStationEntity(station));
    }

    @PostMapping(value = "/constructor/addLink", produces = "application/json")
    @Transactional
    public ResponseEntity addLink(@NotNull @ModelAttribute StationEntity.Link link, Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(link.getStation(), user);
        Block blockFrom = blockService.findByNameAndStation(link.getBlockFrom(), station);
        Block blockTo = blockService.findByNameAndStation(link.getBlockTo(), station);
        if (station == null) {
            //TODO станция не найдена
            return ResponseEntity.ok(null);
        } else if (linkRepository.getByBlockFromAndAndBlockTo(blockFrom, blockTo) != null) {
            //TODO уже существует
            return ResponseEntity.ok(null);
        }
        Link newLink = new Link();
        newLink.setBlockFrom(blockFrom);
        newLink.setBlockTo(blockTo);
//        linkRepository.addLink(newLink);
        LinkPrimary linkPrimary = new LinkPrimary();
        linkPrimary.setBlockFromId(blockFrom.getId());
        linkPrimary.setBlockToId(blockTo.getId());
        newLink.setKey(linkPrimary);
        linkRepository.saveAndFlush(newLink);
        return ResponseEntity.ok(getStationEntity(station));
    }

    private StationEntity getStationEntity(Station station) {
        List<StationEntity.Way> ways = new ArrayList<>();
        List<StationEntity.Block> blocks = new ArrayList<>();
        List<StationEntity.Link> links = new ArrayList<>();

        for (Way way : wayService.getWayByStation(station)) {
            ways.add(new StationEntity.Way(way.getNumber(), station.getName()));
        }
        Map<Block, StationEntity.Block> blockNewBlockMap = new HashMap<>();
        for (Block block : station.getBlocks()) {
            StationEntity.Block newBlock = new StationEntity.Block(block.getName(), block.getLength(), block.getWay().getNumber(), block.getPlatformNumber(), station.getName());
            blockNewBlockMap.put(block, newBlock);
            blocks.add(newBlock);
        }
        for (Block blockFrom : station.getBlocks()) {
            List<Link> linkList = linkRepository.getLinksByBlockFrom(blockFrom);
            if (linkList == null) {
                break;
            }
            for (Link upperLink : linkList) {
                Block blockTo = upperLink.getBlockTo();
                blockNewBlockMap.get(blockFrom).getUpperBlocks().add(blockNewBlockMap.get(blockTo).getName());
                StationEntity.Link link = new StationEntity.Link(blockNewBlockMap.get(blockFrom).getName(), blockNewBlockMap.get(blockTo).getName(), station.getName());
                links.add(link);
            }
        }
        return new StationEntity(station.getName(), ways, blocks, links);
    }

}
