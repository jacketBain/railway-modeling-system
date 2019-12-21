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

        Map<Block,List<Event>> blockEventsMap = new HashMap<>();
        for (Block block : station.getTopology().getBlocks()) {
            blockEventsMap.put(block, new ArrayList<>());
        }

        for (Claim claim : schedule.getClaims()) {
            blockEventsMap = schedule.placeClaim(claim, new ArrayList<>(), blockEventsMap);
            if (blockEventsMap == null) {
                throw new ScheduleException("Не удалось проложить поезд " + claim.getShedule().getTrain());
            }
        }

        schedule.buildEvents(blockEventsMap);

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

    private Map<Block,List<Event>> placeClaim(@NotNull Claim claim, List<Train> conflictTrains, Map<Block,List<Event>> blockEventsMap) throws ScheduleException {
        for (Trip trip : claim.getTrips()) {
            PlaceAttempt placeAttempt = new PlaceAttempt(claim, trip, conflictTrains, blockEventsMap);
            Map<Block,List<Event>> newBlockEventsMap = placeTrip(placeAttempt);
            if (newBlockEventsMap != null) {
                return newBlockEventsMap;
            }
        }
        return null;
    }

    private Map<Block,List<Event>> placeTrip(PlaceAttempt placeAttempt) throws ScheduleException {
        List<Train> conflictTrains = tryPlaceTrip(placeAttempt);
        if (conflictTrains == null) {
            return null;
        } else if (conflictTrains.size() == 0) {
            return placeAttempt.getBlockEventsMap();
        } else {
            Map<Block,List<Event>> result;
            for (Train conflictTrain : conflictTrains) {
                Claim conflictClaim = getClaimByTrain(conflictTrain);

                Map<Block,List<Event>> newBlockEventsMapWithoutConflictTrain = copyBlockEventsMap(placeAttempt.getBlockEventsMap());
                removeTrainEvents(newBlockEventsMapWithoutConflictTrain, conflictTrain);

                List<Train> newConflictTrains = new ArrayList<>(placeAttempt.getConflictTrains());
                newConflictTrains.add(placeAttempt.getClaim().getShedule().getTrain());

                result = placeClaim(conflictClaim, newConflictTrains, newBlockEventsMapWithoutConflictTrain);
                if (result == null) {
                    return null;
                }
            }
            return placeAttempt.getBlockEventsMap();
        }
    }

    private List<Train> tryPlaceTrip(PlaceAttempt placeAttempt) {
        Shedule shedule = placeAttempt.getClaim().getShedule();
        Train train = shedule.getTrain();
        int trainSpeed = train.getTrainType().getSpeed();
        Trip trip = placeAttempt.getTrip();
        List<Block> tripBlocks = trip.getBlocks();
        Block platformBlock = trip.getPlatformBlock();
        Map<Block,List<Event>> blockEventsMap = placeAttempt.getBlockEventsMap();

        int tripBeforeStayLength = 0;
        for (Block tripBlock : tripBlocks) {
            tripBeforeStayLength += tripBlock.getLength();
            if (tripBlock == platformBlock) {
                break;
            }
        }
        //длина 1 вагона 16 метров
        tripBeforeStayLength *= 16;

        long arriveTime = shedule.getArriveTime();
        long departureTime = shedule.getDepartureTime();
        long currentTime = arriveTime - tripBeforeStayLength / trainSpeed * 1000; //время появления поезда на станции
        long freeTime;
        List<Train> conflictTrains = new ArrayList<>();

        for (Block block : tripBlocks) {
            List<Event> blockEvents = blockEventsMap.get(block);
            long occupyTime = currentTime;

            if (block == platformBlock) {
                currentTime = departureTime;
                freeTime = currentTime + (train.getLength() * 16 * 1000 / trainSpeed);
            } else {
                freeTime = currentTime + ((block.getLength() + train.getLength()) * 16 * 1000 / trainSpeed);
            }
            currentTime = currentTime + (block.getLength() * 16 * 1000 / trainSpeed);

            List<Train> newConflictTrains = findConflictTrains(blockEvents, occupyTime, freeTime);
            for (Train newConflictTrain : newConflictTrains) {
                if (placeAttempt.getConflictTrains().contains(newConflictTrain)) {
                    return null;
                } else if (!conflictTrains.contains(newConflictTrain)) {
                    conflictTrains.add(newConflictTrain);
                }
            }

            if (block == platformBlock) {
                blockEvents.add(new Event(EventType.STAY, train, block, occupyTime));
            } else {
                blockEvents.add(new Event(EventType.OCCUPY, train, block, occupyTime));
            }
            blockEvents.add(new Event(EventType.FREE, train, block, freeTime));
        }
        return conflictTrains;
    }

    private List<Train> findConflictTrains(@NotNull List<Event> blockEvents, long occupyTime, long freeTime) {
        List<Train> conflictTrains = new ArrayList<>();
        for (Event blockEvent : blockEvents) {
            long eventTime = blockEvent.getTime();
            if (eventTime >= occupyTime && eventTime <= freeTime) {
                Train conflictTrain = blockEvent.getTrain();
                if (!conflictTrains.contains(conflictTrain)) {
                    conflictTrains.add(conflictTrain);
                }
            }
        }
        return conflictTrains;
    }

    private Map<Block,List<Event>> copyBlockEventsMap(Map<Block,List<Event>> blockEventsMap) {
        Map<Block,List<Event>> newBlockEventsMap = new HashMap<>();
        for (Map.Entry<Block, List<Event>> blockEventsEntry : blockEventsMap.entrySet()) {
            List<Event> blockEvents = new ArrayList<>(blockEventsEntry.getValue());
            newBlockEventsMap.put(blockEventsEntry.getKey(), blockEvents);
        }
        return newBlockEventsMap;
    }

    private void removeTrainEvents(Map<Block,List<Event>> blockEventsMap, Train train) {
        for (Map.Entry<Block, List<Event>> blockEventsEntry : blockEventsMap.entrySet()) {
            List<Event> blockEvents = blockEventsEntry.getValue();
            blockEvents.removeIf(event -> event.getTrain() == train);
        }
    }

    private Claim getClaimByTrain(@NotNull Train train) throws ScheduleException {
        for (Claim claim : claims) {
            if(claim.getShedule().getTrain() == train) {
                return claim;
            }
        }
        throw new ScheduleException("Не найден запрос на прокладку поезда " + train);
    }

    private void buildEvents(Map<Block,List<Event>> blockEventsMap) {
        for (Map.Entry<Block, List<Event>> blockListEntry : blockEventsMap.entrySet()) {
            events.addAll(blockListEntry.getValue());
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
            List<Event> trainEvents = trainEventsEntry.getValue();
            trainEvents.sort(Comparator.comparing(e -> new Date(e.getTime())));
            for (int i = 0; i < trainEvents.size(); i++) {
                Event event = trainEvents.get(i);
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
