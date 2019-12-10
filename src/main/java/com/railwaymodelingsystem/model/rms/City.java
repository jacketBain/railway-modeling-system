package com.railwaymodelingsystem.model.rms;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "city", schema = "public")
public class City implements Serializable {
    @Id
    @Getter
    @Setter
    @NotNull
    @Column(name = "name")
    private String name;
}
