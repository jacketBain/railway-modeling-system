package com.railwaymodelingsystem.rms;

import lombok.Getter;

public enum TrainType {
    CARGO("Грузовой", 1, 10),
    PASSENGER("Пассажирский", 1, 10),
    SAPSAN("Сапсан", 1, 10),
    SUBURBAN("Пригородный", 1, 10);

    @Getter
    private String name;

    @Getter
    private int minLength;

    @Getter
    private int maxLength;

    TrainType (String name, Integer minLength, Integer maxLength) {
        this.name = name;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public String toString() {
        return name;
    }
}
