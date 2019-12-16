package com.railwaymodelingsystem.rms;

import com.railwaymodelingsystem.rms.RMSException.TopologyException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Topology {

    @NotNull
    @Getter
    List<Way> ways;

    @NotNull
    @Getter
    private List<Block> blocks;

    @NotNull
    @Getter
    private List<Block> downDrivewayBlocks;

    @NotNull
    @Getter
    private List<Block> upperDrivewayBlocks;

    public static Topology create(@NotNull List<Way> ways, @NotNull List<Block> blocks) throws TopologyException {
        if (ways.size() != 0) {
            if (blocks.size() >= 3) {
                List<Block> downDrivewayBlocks = new ArrayList<>();
                List<Block> upperDrivewayBlocks = new ArrayList<>();
                for (Block block : blocks) {
                    if (block.getDownBlocks().size() == 0) {
                        downDrivewayBlocks.add(block);
                    } else if (block.getUpperBlocks().size() == 0) {
                        upperDrivewayBlocks.add(block);
                    }
                }
                if (downDrivewayBlocks.size() == 0) {
                    throw new TopologyException("На станции отсутствуют левые подъездные блоки");
                } else if (upperDrivewayBlocks.size() == 0) {
                    throw new TopologyException("На станции отсутствуют правые подъездные блоки");
                }
                return new Topology(ways, blocks, downDrivewayBlocks, upperDrivewayBlocks);
            } else {
                throw new TopologyException("Станция должна содержать минимум 3 блока");
            }
        } else {
            throw new TopologyException("Станция должна содержать минимум 1 путь");
        }
    }

    public boolean check() {

        return true;
    }
}
