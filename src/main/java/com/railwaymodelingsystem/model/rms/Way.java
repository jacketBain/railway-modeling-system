package com.railwaymodelingsystem.model.rms;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "Way", schema = "public")
public class Way implements Serializable {
    @Id
    @Getter
    @Setter
    @NotNull
    @Column(name = "way_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @NotNull
    @Column(name = "way_number", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer number;

    @Getter
    @Setter
    @OneToMany(mappedBy = "way", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Shedule> shedules;

    @Getter
    @Setter
    @OneToMany(mappedBy = "way", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Block> blocks;

}
