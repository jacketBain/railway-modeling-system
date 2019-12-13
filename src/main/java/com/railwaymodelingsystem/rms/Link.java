package com.railwaymodelingsystem.rms;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class Link extends TopologyElement {

    @NotNull
    @Getter
    private Block blockFrom;

    @NotNull
    @Getter
    private Block blockTo;
}
