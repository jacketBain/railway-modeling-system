package com.railwaymodelingsystem.model.rms;

import com.railwaymodelingsystem.model.rms.compositeKey.ShedulePrimary;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    @MapsId("stationId")
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "station_id", referencedColumnName = "station_id", foreignKey = @ForeignKey(name = "FK_Station_Id"))
    private Station station;

    @Getter
    @Setter
    @Column(name = "train_length")
    private Integer trainLength;

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    @Column(name = "arrive_time")
    private Date arriveTime;

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    @Column(name = "departure_time")
    private Date departureTime;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "train_type", nullable = false, foreignKey = @ForeignKey(name = "FK_TrainType"))
    private TrainType trainType;
}
