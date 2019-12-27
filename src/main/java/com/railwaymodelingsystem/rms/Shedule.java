package com.railwaymodelingsystem.rms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class Shedule {

    @NotNull
    @Getter
    private Train train;

    @NotNull
    @Getter
    private Way way;

    @NotNull
    @Getter
    private Integer platformNumber;

    @NotNull
    @Getter
    private Long arriveTime;

    @NotNull
    @Getter
    private Long departureTime;
}
