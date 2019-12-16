package com.railwaymodelingsystem.model.rms.compositeKey;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@EqualsAndHashCode
@ToString
@Embeddable
public class ShedulePrimary implements Serializable {
    @Getter
    @Setter
    @Column(name = "train_number")
    private Integer trainNumber;

    @Getter
    @Setter
    @Column(name = "station_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stationId;
}