package com.railwaymodelingsystem.model.rms;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "train_type", schema = "public")
public class TrainType implements Serializable {
    @Id
    @Getter
    @Setter
    @NotNull
    @Column(name = "type_name")
    private String name;

    @Getter
    @Setter
    @NotNull
    @Column(name = "max_length", nullable = false)
    private Integer maxLength;

    @Getter
    @Setter
    @NotNull
    @Column(name = "min_length", nullable = false)
    private Integer minLength;

    @Getter
    @Setter
    @OneToMany(mappedBy = "trainType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Shedule> shedules;
}
