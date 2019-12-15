package com.railwaymodelingsystem.rms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class Trip {

    @NotNull
    @Getter
    private List<Block> blocks;

    @Getter
    private Block platformBlock;

    @Override
    public String toString() {
        StringBuilder tripString = new StringBuilder();
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            tripString.append(block.toString());
            if (i + 1 < blocks.size()) {
                tripString.append('-');
            }
        }
        return "Маршрут " + tripString.toString() + " со стоянкой на " + platformBlock;
    }
}
