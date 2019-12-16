package com.railwaymodelingsystem.rms;

import lombok.Getter;

public enum Direction {
    EVEN("Чётное"),
    ODD("Нечётное");

    @Getter
    private String name;

    Direction(String name) {
        this.name = name;
    }
}
