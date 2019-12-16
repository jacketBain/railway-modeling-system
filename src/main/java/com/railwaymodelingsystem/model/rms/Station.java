package com.railwaymodelingsystem.model.rms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.railwaymodelingsystem.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "station", schema = "public")
public class Station implements Serializable {
    @Id
    @Getter
    @NotNull
    @Column(name = "station_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Getter
    @Setter
    @NotNull
    @Column(name = "station_name", nullable = false, unique = true)
    private String name;

    @Getter
    @Setter
    @NotNull
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name="user_login", nullable = false, foreignKey = @ForeignKey(name = "FK_Login"))
    private User user;

    @Getter
    @Setter
    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Block> blocks;
}
