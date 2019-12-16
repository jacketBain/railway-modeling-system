package com.railwaymodelingsystem.rms.RMSException;

public class StationException extends RMSException {

    public StationException (String message) {
        setMessage("Ошибка станции:\n" + message);
    }
}
