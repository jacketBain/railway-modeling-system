package com.railwaymodelingsystem.model.rms;

import antlr.Utils;
import com.railwaymodelingsystem.model.rms.compositeKey.ShedulePrimary;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "shedule", schema = "public")
public class Shedule implements Serializable {
    @Getter
    @Setter
    @EmbeddedId
    private ShedulePrimary key;

    @Getter
    @Setter
    @NotNull
    @MapsId("stationId")
    @ManyToOne(optional=false)
    @JoinColumn(name = "station_id", nullable = false, referencedColumnName = "station_id", foreignKey = @ForeignKey(name = "FK_StationId"))
    private Station station;

    @Getter
    @Setter
    @Column(name = "platform_number")
    private Integer platformNumber;

    @Getter
    @Setter
    @Column(name = "shedule_train_length", nullable = false)
    private Integer trainLength;

    @Getter
    @Setter
    @NotNull
    @Column(name = "shedule_arrive_time")
    private Time arriveTime;

    @Getter
    @Setter
    @NotNull
    @Column(name = "shedule_departure_time")
    private Time departureTime;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "city_from", nullable = false, foreignKey = @ForeignKey(name = "FK_SheduleCityFrom"))
    private City cityFrom;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "city_to", nullable = false, foreignKey = @ForeignKey(name = "FK_SheduleCityTo"))
    private City cityTo;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional=false, cascade=CascadeType.MERGE)
    @JoinColumn(name = "shedule_train_type", nullable = false, foreignKey = @ForeignKey(name = "FK_SheduleTrainType"))
    private TrainType trainType;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional=false, cascade=CascadeType.MERGE)
    @JoinColumn(name = "shedule_way_number", nullable = false, foreignKey = @ForeignKey(name = "FK_SheduleWay"))
    private Way way;

    public String getRoute(){
        return getCityFrom() + " - " + getCityTo();
    }

    public String arriveTimeToString() {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        return localDateFormat.format(arriveTime);
    }

    public String departureTimeToString() {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        return localDateFormat.format(departureTime);
    }
}
