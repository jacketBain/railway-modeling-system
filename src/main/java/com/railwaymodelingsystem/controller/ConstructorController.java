package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.model.AjaxResponseBody;
import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Link;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.model.rms.Way;
import com.railwaymodelingsystem.model.rms.compositeKey.LinkPrimary;
import com.railwaymodelingsystem.service.*;
import com.railwaymodelingsystem.utils.StringValidator;
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
    LinkService linkService;

    @Autowired
    StationService stationService;

    @Autowired
    UserService userService;

    @GetMapping("/startConstructor/checkName")
    public ResponseEntity<AjaxResponseBody> checkStationName(@RequestParam(value = "name")String stationName, Principal principal){
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
    public ResponseEntity getStation(@RequestParam("name") String name, Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(name, user);
        if(station == null)
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
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
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
        } else if (way.getNumber() <= 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Номер пути не может быть отрицательным или 0", "ERROR"));
        } else if (way.getNumber() > 15){
            return ResponseEntity.ok(new AjaxResponseBody("Нельза создать больше 15 путей", "ERROR"));
        } else if (wayService.getByWayAndStation(way.getNumber(), station).size() != 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Путь уже существует", "ERROR"));
        } else if (way.getNumber() != 1 && wayService.getByWayAndStation(way.getNumber() - 1, station) == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Вы пытаетесь создать непоследовательный путь", "ERROR"));
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
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
        } else if(!StringValidator.checkBlockName(block.getName())) {
            return ResponseEntity.ok(new AjaxResponseBody("Некорректное имя блок-участка", "ERROR"));
        } else if(block.getLength() < 1 || block.getLength() > 71) {
            return ResponseEntity.ok(new AjaxResponseBody("Длина блок-участка может быть в диапазоне от 1 до 71", "ERROR"));
        } else if(wayService.getByWayAndStation(block.getWay(), station).size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Невозможно добавить блок-участок на несуществующий путь", "ERROR"));
        } else if(block.getPlatformNumber() != null && blockService.findByStationAndPlatform(station, block.getPlatformNumber()).size() >= 2) {
            return ResponseEntity.ok(new AjaxResponseBody("К платформе не может прилегать больше 2-х путей", "ERROR"));
        } else if (blockService.findByNameAndStation(block.getName(), station).size() == 1) {
            return ResponseEntity.ok(new AjaxResponseBody("Блок уже существует", "ERROR"));
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
        List<Way> wayList = wayService.getByWayAndStation(block.getWay(), station);
        if (wayList.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Нет такого пути", "ERROR"));
        }
        newBlock.setWay(wayList.get(0));
        blockService.addBlock(newBlock);
        return ResponseEntity.ok(getStationEntity(station));
    }

    @PostMapping(value = "/constructor/addLink", produces = "application/json")
    @Transactional
    public ResponseEntity addLink(@NotNull @ModelAttribute StationEntity.Link link, Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(link.getStation(), user);
        List<Block> blockFromList = blockService.findByNameAndStation(link.getBlockFrom(), station);
        List<Block> blockToList = blockService.findByNameAndStation(link.getBlockTo(), station);
        if (station == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
        } else if (blockService.findByNameAndStation(link.getBlockFrom(), station).size() == 0 ||
                blockService.findByNameAndStation(link.getBlockTo(), station).size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Один или несколько блок-участков не существует", "ERROR"));
        } else if (linkService.getByBlockFromOrBlockTo(blockFromList.get(0), blockToList.get(0)).size() != 0 || linkService.getByBlockFromAndBlockTo(blockToList.get(0), blockFromList.get(0)).size() != 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Блоки уже связаны или провести связь невозможно", "ERROR"));
        } else if (linkService.getByBlockFromOrBlockTo(blockFromList.get(0), blockToList.get(0)).size() == 1) {
            return ResponseEntity.ok(new AjaxResponseBody("Уже существует обратная связь", "ERROR"));
        } else if (link.getBlockFrom() == link.getBlockTo()) {
            return ResponseEntity.ok(new AjaxResponseBody("Нельзя соединить блок самим с собой", "ERROR"));
        }
        Link newLink = new Link();
        newLink.setBlockFrom(blockFromList.get(0));
        newLink.setBlockTo(blockToList.get(0));
        LinkPrimary linkPrimary = new LinkPrimary();
        linkPrimary.setBlockFromId(blockFromList.get(0).getId());
        linkPrimary.setBlockToId(blockToList.get(0).getId());
        newLink.setKey(linkPrimary);
        linkService.addLink(newLink);
        return ResponseEntity.ok(getStationEntity(station));
    }

    @PostMapping("/constructor/editBlock")
    @Transactional
    public ResponseEntity editBlock(@RequestParam(value = "old_name")String blockName,
                                                      @NotNull @ModelAttribute StationEntity.Block block,
                                                      Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(block.getStation(), user);
        List<Block> newBlockList = blockService.findByNameAndStation(block.getName(), station);
        List<Block> oldBlockList = blockService.findByNameAndStation(blockName, station);
        if (station == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
        } else if(!StringValidator.checkBlockName(block.getName())) {
            return ResponseEntity.ok(new AjaxResponseBody("Некорректное имя блок-участка", "ERROR"));
        } else if(block.getLength() < 1 || block.getLength() > 71) {
            return ResponseEntity.ok(new AjaxResponseBody("Длина блок-участка может быть в диапазоне от 1 до 71", "ERROR"));
        } else if(wayService.getByWayAndStation(block.getWay(), station).size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Невозможно добавить блок-участок на несуществующий путь", "ERROR"));
        } else if(!blockName.equals(block.name) && block.getPlatformNumber() != null && blockService.findByStationAndPlatform(station, block.getPlatformNumber()).size() >= 2) {
            return ResponseEntity.ok(new AjaxResponseBody("К платформе не может прилегать больше 2-х путей", "ERROR"));
        } else if (newBlockList.size() == 1 && !oldBlockList.get(0).getName().equals(newBlockList.get(0).getName())) {
            return ResponseEntity.ok(new AjaxResponseBody("Блок уже существует", "ERROR"));
        }
        Block oldBlock = oldBlockList.get(0);
        oldBlock.setName(block.getName());
        oldBlock.setLength(block.getLength());
        if (block.getPlatformNumber() != null) {
            oldBlock.setHasPlatform(true);
            oldBlock.setPlatformNumber(block.getPlatformNumber());
        } else {
            oldBlock.setHasPlatform(false);
            oldBlock.setPlatformNumber(null);
        }
        List<Way> wayList = wayService.getByWayAndStation(block.getWay(), station);
        if (wayList.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Нет такого пути", "ERROR"));
        }
        oldBlock.setWay(wayList.get(0));
        blockService.editBlock(oldBlock);
        return ResponseEntity.ok(getStationEntity(station));
    }

    @PostMapping("/constructor/editWay")
    @Transactional
    public ResponseEntity editWay(@RequestParam(value = "old_number")String wayNumber,
                                    @NotNull @ModelAttribute StationEntity.Way way,
                                    Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(way.getStation(), user);
        List<Way> newWayList = wayService.getByWayAndStation(way.getNumber(), station);
        if (!StringValidator.checkWayNumber(wayNumber)) {
            return ResponseEntity.ok(new AjaxResponseBody("Неправильный номер пути", "ERROR"));
        }
        List<Way> oldWayList = wayService.getByWayAndStation(Integer.parseInt(wayNumber), station);
        if (station == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
        } else if(oldWayList.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Путь не существует", "ERROR"));
        } else if (newWayList.size() == 1 && !oldWayList.get(0).getNumber().equals(newWayList.get(0).getNumber())) {
            return ResponseEntity.ok(new AjaxResponseBody("Путь уже существует", "ERROR"));
        }
        Way oldWay = oldWayList.get(0);
        oldWay.setNumber(way.getNumber());
        wayService.editWay(oldWay);
        return ResponseEntity.ok(getStationEntity(station));
    }

    @GetMapping("/constructor/removeBlock")
    @Transactional
    public ResponseEntity removeBlock(@RequestParam(value = "name") String blockName,
                                      @RequestParam(value = "station") String stationName,
                                    Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(stationName, user);
        if (station == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
        } else if(!StringValidator.checkBlockName(blockName)) {
            return ResponseEntity.ok(new AjaxResponseBody("Неправильное имя блок-участка", "ERROR"));
        }
        List<Block> blockList = blockService.findByNameAndStation(blockName, station);
        if(blockList.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Блок участок не существует", "ERROR"));
        }
        Block block = blockList.get(0);
        List<Link> links = linkService.getByBlockFromOrBlockTo(block, block);

        for (Link link : links) {
            linkService.deleteLink(link);
        }

        blockService.deleteBlock(block);

        return ResponseEntity.ok(getStationEntity(station));
    }

    @GetMapping("/constructor/removeWay")
    @Transactional
    public ResponseEntity removeWay(@RequestParam(value = "number")String wayNumber,
                                      @RequestParam(value = "station")String stationName,
                                      Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(stationName, user);
        if (!StringValidator.checkWayNumber(wayNumber)) {
            return ResponseEntity.ok(new AjaxResponseBody("Неверный номер пути", "ERROR"));
        }
        List<Way> wayList = wayService.getByWayAndStation(Integer.parseInt(wayNumber), station);
        if (station == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
        } else if(wayList.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Путь не существует", "ERROR"));
        }
        Way way = wayList.get(0);
        List<Block> blockList = blockService.findAllByStationAndWay(station, way);

        for (Block block : blockList) {
            List<Link> links = linkService.getByBlockFromOrBlockTo(block, block);

            for (Link link : links) {
                linkService.deleteLink(link);
            }

            blockService.deleteBlock(block);
        }

        wayService.removeWay(way);

        return ResponseEntity.ok(getStationEntity(station));
    }

    @GetMapping("/constructor/removeLink")
    @Transactional
    public ResponseEntity removeLink(@RequestParam(value = "blockFrom") String blockFromName,
                                      @RequestParam(value = "blockTo") String blockToName,
                                      @RequestParam(value = "station") String stationName,
                                      Principal principal) {
        User user = userService.getByName(principal.getName());
        Station station = stationService.getStationByNameAndUser(stationName, user);
        if (station == null) {
            return ResponseEntity.ok(new AjaxResponseBody("Неверная станция", "ERROR"));
        } else if (!StringValidator.checkBlockName(blockFromName)) {
            return ResponseEntity.ok(new AjaxResponseBody("Неправильное имя первого блок-участка", "ERROR"));
        } else if (!StringValidator.checkBlockName(blockToName)) {
            return ResponseEntity.ok(new AjaxResponseBody("Неправильное имя второго блок-участка", "ERROR"));
        }
        List<Block> blockFromList = blockService.findByNameAndStation(blockFromName, station);
        if(blockFromList.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Блок участок не существует", "ERROR"));
        }
        List<Block> blockToList = blockService.findByNameAndStation(blockToName, station);
        if(blockToList.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Блок участок не существует", "ERROR"));
        }
        Block blockFrom = blockFromList.get(0);
        Block blockTo = blockToList.get(0);
        List<Link> links = linkService.getByBlockFromAndBlockTo(blockFrom, blockTo);

        if (links.size() == 0) {
            return ResponseEntity.ok(new AjaxResponseBody("Такой связи нет", "ERROR"));
        }
        linkService.deleteLink(links.get(0));
        return ResponseEntity.ok(getStationEntity(station));
    }

    private StationEntity getStationEntity(Station station) {
        List<StationEntity.Way> ways = new ArrayList<>();
        List<StationEntity.Block> blocks = new ArrayList<>();
        List<StationEntity.Link> links = new ArrayList<>();

        for (Way way : wayService.getByStation(station)) {
            ways.add(new StationEntity.Way(way.getNumber(), station.getName()));
        }
        Map<Block, StationEntity.Block> blockNewBlockMap = new HashMap<>();
        for (Block block : station.getBlocks()) {
            StationEntity.Block newBlock = new StationEntity.Block(block.getName(), block.getLength(), block.getWay().getNumber(), block.getPlatformNumber(), station.getName());
            blockNewBlockMap.put(block, newBlock);
            blocks.add(newBlock);
        }
        for (Block blockFrom : station.getBlocks()) {
            List<Link> linkList = linkService.getLinksByBlockFrom(blockFrom);
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
