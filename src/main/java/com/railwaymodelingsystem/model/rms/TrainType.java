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
    @OneToMany(mappedBy = "trainType", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Collection<Shedule> shedules;
}
