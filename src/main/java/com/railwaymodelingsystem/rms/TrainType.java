package com.railwaymodelingsystem.rms;

import com.railwaymodelingsystem.rms.RMSException.SheduleException;
import lombok.Getter;

public enum TrainType {
    CARGO("Грузовой",33),
    PASSENGER("Пассажирский",  25),
    SAPSAN("Сапсан",  69),
    SUBURBAN("Пригородный", 27);

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
