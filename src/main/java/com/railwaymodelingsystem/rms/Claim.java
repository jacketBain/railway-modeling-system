package com.railwaymodelingsystem.rms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class Claim {

    @NotNull
    @Getter
    private Shedule shedule;

    @NotNull
    @Getter
    private List<Trip> trips;

    @Override
    public String toString() {
        StringBuilder tripsString = new StringBuilder();
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            tripsString.append(trip.toString());
            if (i + 1 < trips.size()) {
                tripsString.append('\n');
            }
        }
        return "Заявка на прокладку поезда " + shedule.getTrain() + ", список маршрутов:\n" + tripsString;
    }
}
