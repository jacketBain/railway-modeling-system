package com.railwaymodelingsystem.rms;

public enum EventType {
    OCCUPY("Занятие"),
    FREE("Освобождение"),
    STAY("Стоянка");

    private String name;

    EventType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
