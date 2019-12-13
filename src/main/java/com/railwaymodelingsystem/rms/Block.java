package com.railwaymodelingsystem.rms;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@AllArgsConstructor
public class Block extends TopologyElement {

    @NotNull
    @Getter
    private Integer id;

    @NotNull
    @Getter
    @Setter
    private String name;

    @NotNull
    @Getter
    @Setter
    private Integer length;

    @NotNull
    @Getter
    private Station station;

    @NotNull
    @Getter
    @Setter
    private Way way;

    @NotNull
    @Getter
    private List<Block> upperBlocks;

    @NotNull
    @Getter
    private List<Block> downBlocks;

    @NotNull
    @Getter
    private Boolean hasPlatform;

    @Nullable
    @Getter
    private Integer platformNumber;

    public void setHasPlatform (Boolean hasPlatform) {
        this.hasPlatform = hasPlatform;
        this.platformNumber = null;
    }

    public void setPlatformNumber(Integer platformNumber) {
        if (hasPlatform) {
            this.platformNumber = platformNumber;
        } else {
            throw new IllegalStateException("У блок-участка " + toString() + "нет платформы");
        }
    }
}
