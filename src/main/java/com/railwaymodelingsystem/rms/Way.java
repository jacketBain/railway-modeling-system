package com.railwaymodelingsystem.rms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class Way {

    @NotNull
    @Getter
    private Integer number;
}
