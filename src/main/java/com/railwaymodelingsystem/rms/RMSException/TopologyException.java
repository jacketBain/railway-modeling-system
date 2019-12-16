package com.railwaymodelingsystem.rms.RMSException;

public class TopologyException extends RMSException {

    public TopologyException(String message) {
        setMessage("Во время построения топологии произошла ошибка:\n" + message);
    }
}
