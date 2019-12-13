package com.railwaymodelingsystem.rms;

import com.railwaymodelingsystem.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class Station {

    @NotNull
    @Getter
    private Integer id;

    @NotNull
    @Getter
    @Setter
    private String name;

    @NotNull
    @Getter
    private User user;

    @NotNull
    @Getter
    private Topology topology;
}
