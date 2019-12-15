package com.railwaymodelingsystem.rms;

import com.sun.istack.Nullable;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString
public class Block {

    @NotNull
    @Getter
    private String name;

    @NotNull
    @Getter
    private Integer length;

    @NotNull
    @Getter
    private Way way;

    @NotNull
    @Getter
    private List<Block> upperBlocks = new ArrayList<>();

    @NotNull
    @Getter
    private List<Block> downBlocks = new ArrayList<>();

    @NotNull
    @Getter
    private Boolean hasPlatform;

    @Nullable
    @Getter
    private Integer platformNumber;

    public Block(String name, Integer length, Way way) {
        this.name = name;
        this.length = length;
        this.way = way;
        this.hasPlatform = false;
    }

    public Block(String name, Integer length, Way way, Integer platformNumber) {
        this.name = name;
        this.length = length;
        this.way = way;
        this.hasPlatform = true;
        this.platformNumber = platformNumber;
    }

    @Override
    public String toString() {
        return name;
    }
}
