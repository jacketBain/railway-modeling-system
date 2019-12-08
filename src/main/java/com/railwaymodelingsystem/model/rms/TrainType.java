package com.railwaymodelingsystem.model.rms;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "train_type", schema = "public")
public class TrainType implements Serializable {
    @Id
    @Column(name = "type_name")
    private String name;

    @Getter
    @Setter
    @Column(name = "max_length")
    private Integer maxLength;

    @Getter
    @Setter
    @Column(name = "min_length")
    private Integer minLength;

    @OneToMany(mappedBy = "trainType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Shedule> shedules;
}
