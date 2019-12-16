package com.railwaymodelingsystem.rms;

import com.railwaymodelingsystem.rms.RMSException.ScheduleException;
import com.railwaymodelingsystem.rms.RMSException.StationException;
import com.railwaymodelingsystem.rms.RMSException.TopologyException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Scheduler {

    private static Station station;

    private Scheduler() { }

    public static void main(String[] args) {
        try {
            station = createStation();
        } catch (Exception e) {
            System.out.println("Creating station error: " + e.getMessage() + "\n");
            e.printStackTrace();
        }

        Schedule schedule = null;
        if (station != null) {
            try {
                schedule = getSchedule();
            } catch (ScheduleException e) {
                System.out.println("Build schedule exception: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }

        if (schedule != null) {
            System.out.println("[INFO] Получен график движения поездов на станции " + station + ":\n" + schedule);
            System.out.println("\nСписок событий:");
            schedule.printEvents();
        }
    }

    public static Schedule getSchedule() throws ScheduleException {
        if (station != null) {
            return Schedule.buildSchedule(station);
        } else {
            throw new ScheduleException("Станция не создана");
        }
    }

    public static Station createStation() throws ParseException, StationException, TopologyException {
        City cityFrom = new City("Самара");
        City cityTo = new City("Москва");
        City cityStation = new City("Казань");

        Way way1 = new Way(1);
        Way way2 = new Way(2);

        Block block1 = new Block("1П", 20, way1);
        Block block2 = new Block("1АП", 60, way1, 1);
        Block block3 = new Block("1БП", 20, way1);
        Block block4 = new Block("2П", 20, way2);
        Block block5 = new Block("2АП", 60, way2, 2);
        Block block6 = new Block("2БП", 20, way2);

        block1.getUpperBlocks().add(block2);
        block2.getDownBlocks().add(block1);
        block2.getUpperBlocks().add(block3);
        block3.getDownBlocks().add(block2);

        block4.getUpperBlocks().add(block5);
        block5.getDownBlocks().add(block4);
        block5.getUpperBlocks().add(block6);
        block6.getDownBlocks().add(block5);

        List<Way> ways = new ArrayList<>(Arrays.asList(way1, way2));
        List<Block> blocks = new ArrayList<>(Arrays.asList(block1, block2, block3, block4, block5, block6));

        Topology topology = Topology.create(ways, blocks);

        Train train1 = new Train(1001, TrainType.PASSENGER, 20, cityFrom, cityTo);
        Train train3 = new Train(1003, TrainType.CARGO, 40, cityFrom, cityTo);
        Train train5 = new Train(1005, TrainType.SAPSAN, 10, cityFrom, cityTo);
        Train train7 = new Train(1007, TrainType.SUBURBAN, 5, cityFrom, cityTo);

        Train train2 = new Train(1002, TrainType.CARGO, 20, cityTo, cityFrom);
        Train train4 = new Train(1004, TrainType.PASSENGER, 40, cityTo, cityFrom);
        Train train6 = new Train(1006, TrainType.SAPSAN, 10, cityTo, cityFrom);
        Train train8 = new Train(1008, TrainType.SUBURBAN, 5, cityTo, cityFrom);


        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        Long arriveTime1 = dateFormat.parse("00:10:00").getTime();
        Long departureTime1 = dateFormat.parse("00:30:00").getTime();
        Shedule shedule1 = new Shedule(train1, way1, arriveTime1, departureTime1);
        Shedule shedule2 = new Shedule(train2, way2, arriveTime1, departureTime1);

        Long arriveTime2 = dateFormat.parse("00:40:00").getTime();
        Long departureTime2 = dateFormat.parse("01:10:00").getTime();
        Shedule shedule3 = new Shedule(train3, way1, arriveTime2, departureTime2);
        Shedule shedule4 = new Shedule(train4, way2, arriveTime2, departureTime2);

        Long arriveTime3 = dateFormat.parse("01:20:00").getTime();
        Long departureTime3 = dateFormat.parse("01:50:00").getTime();
        Shedule shedule5 = new Shedule(train5, way1, arriveTime3, departureTime3);
        Shedule shedule6 = new Shedule(train6, way2, arriveTime3, departureTime3);

        Long arriveTime4 = dateFormat.parse("02:00:00").getTime();
        Long departureTime4 = dateFormat.parse("02:30:00").getTime();
        Shedule shedule7 = new Shedule(train7, way1, arriveTime4, departureTime4);
        Shedule shedule8 = new Shedule(train8, way2, arriveTime4, departureTime4);

        List<Shedule> shedules = new ArrayList<>(Arrays.asList(shedule1, shedule2, shedule3, shedule4,
                                                                shedule5, shedule6, shedule7, shedule8));

        return Station.create("Казань", cityStation, topology, shedules);
    }
}
