package com.railwaymodelingsystem.model.rms;

import com.railwaymodelingsystem.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "station", schema = "public")
public class Station implements Serializable {
    @Id
    @Getter
    @NotNull
    @Column(name = "station_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @NotNull
    @Column(name = "station_name", nullable = false)
    private String name;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name="user_login", nullable = false, foreignKey = @ForeignKey(name = "FK_Login"))
    private User user;

    @Getter
    @Setter
    @NotNull
    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="city_name", nullable = false, foreignKey = @ForeignKey(name = "FK_CityName"))
    private City city;
}
