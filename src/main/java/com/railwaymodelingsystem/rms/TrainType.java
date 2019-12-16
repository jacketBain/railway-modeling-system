package com.railwaymodelingsystem.rms;

import com.railwaymodelingsystem.rms.RMSException.SheduleException;
import lombok.Getter;

public enum TrainType {
    CARGO("Грузовой",10),
    PASSENGER("Пассажирский", 10),
    SAPSAN("Сапсан", 10),
    SUBURBAN("Пригородный",10);

    @Getter
    private String name;

    @Getter
    private int maxLength;

    TrainType (String name, Integer maxLength) {
        this.name = name;
        this.maxLength = maxLength;
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
