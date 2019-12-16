package com.railwaymodelingsystem.model.rms;

import com.railwaymodelingsystem.model.rms.compositeKey.ShedulePrimary;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.io.Serializable;
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
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "station_id", nullable = false, referencedColumnName = "station_id", foreignKey = @ForeignKey(name = "FK_Station_Id"))
    private Station station;

    @Getter
    @Setter
    @NotNull
    @Column(name = "shedule_train_length", nullable = false)
    private Integer trainLength;

    @Getter
    @Setter
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "shedule_arrive_time")
    private Date arriveTime;

    @Getter
    @Setter
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "shedule_departure_time")
    private Date departureTime;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_from", nullable = false, foreignKey = @ForeignKey(name = "FK_SheduleCityFrom"))
    private City cityFrom;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_to", nullable = false, foreignKey = @ForeignKey(name = "FK_SheduleCityTo"))
    private City cityTo;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "shedule_train_type", nullable = false, foreignKey = @ForeignKey(name = "FK_SheduleTrainType"))
    private TrainType trainType;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "shedule_way_number", nullable = false, foreignKey = @ForeignKey(name = "FK_SheduleWay"))
    private Way way;
}
