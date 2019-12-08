package com.railwaymodelingsystem.model.rms;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "station", schema = "public")
public class Station implements Serializable {
    @Id
    @Getter
    @Setter
    @Column(name = "station_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @Column(name = "station_name")
    private String name;

    //login

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="city_name", foreignKey = @ForeignKey(name = "FK_CityName"))
    private City city;
}
