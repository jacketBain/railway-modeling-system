package com.railwaymodelingsystem.rms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class Train {

    @NotNull
    @Getter
    private Integer number;

    @NotNull
    @Getter
    private TrainType trainType;

    @NotNull
    @Getter
    private Integer length;

    @NotNull
    @Getter
    private City cityFrom;

    @NotNull
    @Getter
    private City cityTo;

    public Direction getDirection() {
        return number % 2 == 0 ? Direction.EVEN : Direction.ODD;
    }

    @Override
    public String toString() {
        return trainType + " " + number + " " + cityFrom + "-" + cityTo + " " + length + " вагонов";
    }
}
