package com.railwaymodelingsystem.rms;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class City {

    @NotNull
    @Getter
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
