package com.railwaymodelingsystem.rms;

import com.railwaymodelingsystem.rms.RMSException.ScheduleException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Schedule {

    @NotNull
    @Getter
    private Station station;

    @NotNull
    @Getter
    private List<Claim> claims;

    @NotNull
    @Getter
    private List<Event> events = new ArrayList<>();

    private Schedule() { }

    public static Schedule buildSchedule(@NotNull Station station) throws ScheduleException {
        Schedule schedule = new Schedule();
        schedule.station = station;
        schedule.claims = schedule.createClaims();

        schedule.placeClaims();

        return schedule;
    }

    private List<Claim> createClaims() throws ScheduleException {
        List<Claim> claims = new ArrayList<>();
        List<Block> downDrivewayBlocks = station.getTopology().getDownDrivewayBlocks();
        List<Block> upperDrivewayBlocks = station.getTopology().getUpperDrivewayBlocks();
        for (Shedule shedule : station.getShedules()) {
            Train train = shedule.getTrain();
            Direction direction = train.getDirection();
            List<Block> startBlocks = direction == Direction.EVEN ?
                    downDrivewayBlocks : upperDrivewayBlocks;
            List<Trip> trips = new ArrayList<>();
            for (Block startBlock : startBlocks) {
                trips.addAll(createTrips(
                        new ArrayList<>(Collections.singletonList(startBlock)),
                        shedule, direction
                ));
            }
            if (trips.size() == 0) {
                throw new ScheduleException("У поезда " + train + " нет маршрутов, по которым он мог бы проехать станцию");
            } else {
                claims.add(new Claim(shedule, trips));
            }
        }
        return claims;
    }

    private List<Trip> createTrips(List<Block> blocks, Shedule shedule, Direction direction) {
        List<Block> nextBlocks;
        if (direction == Direction.EVEN) {
            nextBlocks = blocks.get(blocks.size() - 1).getUpperBlocks();
        } else {
            nextBlocks = blocks.get(blocks.size() - 1).getDownBlocks();
        }
        List<Trip> trips = new ArrayList<>();
        if (nextBlocks.size() == 0) {
            for (int i = 1; i < blocks.size() - 1; i++) {
                Block block = blocks.get(i);
                if (shedule.getWay() == block.getWay()) {
                    if (shedule.getTrain().getTrainType() == TrainType.CARGO) {
                        trips.add(new Trip(new ArrayList<>(blocks), block));
                    } else if (block.getHasPlatform()) {
                        trips.add(new Trip(new ArrayList<>(blocks), block));
                    }
                }
            }
        } else {
            for (Block nextBlock : nextBlocks) {
                List<Block> blockList = new ArrayList<>(blocks);
                blockList.add(nextBlock);
                trips.addAll(createTrips(blockList, shedule, direction));
            }
        }
        return trips;
    }

    private void placeClaims() {
        for (Claim claim : claims) {
            placeClaim(claim);
        }
    }

    private void placeClaim(Claim claim) {
        Shedule shedule = claim.getShedule();
        Train train = shedule.getTrain();
        Long arriveTime = shedule.getArriveTime();
        Long departureTime = shedule.getDepartureTime();
        Long currentTime = arriveTime;
        for (Trip trip : claim.getTrips()) {
            for (Block block : trip.getBlocks()) {
                if (block == trip.getPlatformBlock()) {
                    events.add(new Event(EventType.STAY, train, block, currentTime));
                    currentTime = departureTime;
                } else {
                    events.add(new Event(EventType.OCCUPY, train, block, currentTime));
                }
                events.add(new Event(EventType.FREE, train, block, currentTime));
            }
        }
    }

    public void printEvents() {
        StringBuilder eventsString = new StringBuilder();
        Map<Train, List<Event>> trainEventsMap = new HashMap<>();
        for (Claim claim : claims) {
            trainEventsMap.put(claim.getShedule().getTrain(), new ArrayList<>());
        }
        for (Event event : events) {
            trainEventsMap.get(event.getTrain()).add(event);
        }
        for (Map.Entry<Train, List<Event>> trainEventsEntry : trainEventsMap.entrySet()) {
            eventsString.append("Поезд " + trainEventsEntry.getKey() + ":\n");
            List<Event> value = trainEventsEntry.getValue();
            for (int i = 0; i < value.size(); i++) {
                Event event = value.get(i);
                eventsString.append(event).append('\n');
            }
        }
        System.out.println(eventsString);
    }

    @Override
    public String toString() {
        StringBuilder claimsString = new StringBuilder();
        for (int i = 0; i < claims.size(); i++) {
            Claim claim = claims.get(i);
            claimsString.append(claim);
            if (i + 1 < claims.size()) {
                claimsString.append('\n');
            }
        }
        return "График движения на станции " + station + ", количество заявок на прокладку поездов "
                + claims.size() + ":\n" + claimsString;
    }
}
