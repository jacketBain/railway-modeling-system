package com.railwaymodelingsystem.rms;

import com.railwaymodelingsystem.rms.RMSException.SheduleException;
import lombok.Getter;

public enum TrainType {
    CARGO("Грузовой",5),
    PASSENGER("Пассажирский",  3),
    SAPSAN("Сапсан",  7),
    SUBURBAN("Пригородный", 4);

    @Getter
    private String name;

    //метр в секунду
    @Getter
    private int speed;

    TrainType (String name, int speed) {
        this.name = name;
        this.speed = speed;
    }

    public static TrainType fromType(com.railwaymodelingsystem.model.rms.TrainType trainType) throws SheduleException {
        switch (trainType.getName()) {
            case "Грузовой":
                return CARGO;
            case "Пассажирский":
                return PASSENGER;
            case "Сапсан":
                return SAPSAN;
            case "Электричка":
                return SUBURBAN;
            default:
                throw new SheduleException("Неизвестный тип поезда " + trainType.getName());
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
