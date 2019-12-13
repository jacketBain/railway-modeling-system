package com.railwaymodelingsystem.rms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
public class Schedule {

    @NotNull
    @Getter
    @Setter
    private Integer trainNumber;

    @NotNull
    @Getter
    @Setter
    private TrainType trainType;

    @NotNull
    @Getter
    @Setter
    private Station station;

    @NotNull
    @Getter
    @Setter
    private Integer wayNumber;

    @NotNull
    @Getter
    @Setter
    private Date arriveTime;

    @NotNull
    @Getter
    @Setter
    private Date departureTime;

    @NotNull
    @Getter
    @Setter
    private Integer trainLength;

    @NotNull
    @Getter
    @Setter
    private City cityFrom;

    @NotNull
    @Getter
    @Setter
    private City cityTo;
}
