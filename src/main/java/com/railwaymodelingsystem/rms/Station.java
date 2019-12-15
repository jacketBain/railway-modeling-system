package com.railwaymodelingsystem.rms;

import com.railwaymodelingsystem.rms.RMSException.StationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Station {

    @NotNull
    @Getter
    private String name;

    @NotNull
    @Getter
    private City city;

    @NotNull
    @Getter
    private Topology topology;

    @NotNull
    @Getter
    private List<Shedule> shedules;

    public static Station create(@NotNull String name, @NotNull City city, @NotNull Topology topology, @NotNull List<Shedule> shedules) throws StationException {
        checkName(name);
        if (shedules.size() != 0) {
            return new Station(name, city, topology, shedules);
        } else {
            throw new StationException("Для станции не предано ни одного расписания");
        }
    }

    private static void checkName(String name) throws StationException {
        int length = name.length();
        if (length >= 2 && length <= 30) {
            for (int i = 0; i < length; i++) {
                char ch = name.charAt(i);
                if (!Character.isAlphabetic(ch) && !Character.isDigit(ch) && !(ch == ' ') && !(ch == '-')) {
                    throw new StationException("Некорректное название станции: символ " + ch );
                }
            }
        } else {
            throw new StationException("Некорректное название станции: длина названия должна быть в диапазоне от 2 до 30 символов");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
