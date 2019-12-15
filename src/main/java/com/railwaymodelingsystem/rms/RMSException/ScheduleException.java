package com.railwaymodelingsystem.rms.RMSException;

public class ScheduleException extends RMSException {

    public ScheduleException(String message) {
        setMessage("Во время построения графика произошла ошибка:\n" + message);
    }
}
