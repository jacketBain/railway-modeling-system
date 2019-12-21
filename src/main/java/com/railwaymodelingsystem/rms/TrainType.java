package com.railwaymodelingsystem.rms;

import com.railwaymodelingsystem.rms.RMSException.SheduleException;
import lombok.Getter;

public enum TrainType {
    CARGO("Грузовой",10, 33),
    PASSENGER("Пассажирский", 10, 25),
    SAPSAN("Сапсан", 10, 69),
    SUBURBAN("Пригородный",10, 27);

    @Getter
    private String name;

    @Getter
    private int maxLength;

    //метр в секунду
    @Getter
    private int speed;

    TrainType (String name, Integer maxLength, int speed) {
        this.name = name;
        this.maxLength = maxLength;
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
            case "SUBURBAN":
                return SUBURBAN;
            default:
                throw new SheduleException("Неизвестный тип поезда");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
