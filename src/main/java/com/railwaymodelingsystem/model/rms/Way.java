package com.railwaymodelingsystem.model.rms;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "Way", schema = "public")
public class Way implements Serializable {
    @Id
    @Getter
    @Column(name = "way_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Getter
    @Setter
    @NotNull
    @Column(name = "way_number", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer number;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional=false, cascade=CascadeType.MERGE)
    @JoinColumn(name = "way_station", nullable = false, referencedColumnName = "station_id", foreignKey = @ForeignKey(name = "FK_WayStationId"))
    private Station station;

    @Getter
    @Setter
    @OneToMany(mappedBy = "way", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Collection<Shedule> shedules;

    @Getter
    @Setter
    @OneToMany(mappedBy = "way", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Collection<Block> blocks;

}
