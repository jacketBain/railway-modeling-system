package com.railwaymodelingsystem.rms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@AllArgsConstructor
public class Event {

    @NotNull
    @Getter
    private EventType eventType;

    @NotNull
    @Getter
    private Train train;

    @NotNull
    @Getter
    private Block block;

    @NotNull
    @Getter
    private Long time;

    @Override
    public String toString() {
        Locale locale = new Locale("ru", "RU");
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);
        return (eventType == EventType.STAY ? "Стоянка на" : eventType.toString()) + " " + block + " в " + dateFormat.format(new Date(time));
    }
}
