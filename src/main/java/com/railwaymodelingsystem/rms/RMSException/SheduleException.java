package com.railwaymodelingsystem.rms.RMSException;

public class SheduleException extends RMSException {

    public SheduleException(String message) {
        setMessage("Во время построения расписания произошла ошибка:\n" + message);
    }
}
