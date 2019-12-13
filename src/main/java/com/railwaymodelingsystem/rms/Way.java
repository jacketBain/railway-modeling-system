package com.railwaymodelingsystem.rms;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class Way {

    @NotNull
    @Getter
    @Setter
    private Integer id;

    @NotNull
    @Getter
    @Setter
    private Integer number;

    public Way (@NotNull Integer id, @NotNull Integer number) {
        this.id = id;
        this.number = number;
    }
}
