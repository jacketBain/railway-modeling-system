package com.railwaymodelingsystem.rms;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceAttempt {

    @NotNull
    @Getter
    private Claim claim;

    @NotNull
    @Getter
    private Trip trip;

    @Getter
    private List<Train> conflictTrains;

    @Getter
    private Map<Block,List<Event>> blockEventsMap = new HashMap<>();

    public PlaceAttempt(@NotNull Claim claim, @NotNull Trip trip, @NotNull List<Train> conflictTrains, @NotNull Map<Block,List<Event>> blockEventsMap) {
        this.claim = claim;
        this.trip = trip;
        this.conflictTrains = new ArrayList<>(conflictTrains);
        this.blockEventsMap = blockEventsMap;
    }
}
