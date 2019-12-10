package com.railwaymodelingsystem.model;

import com.railwaymodelingsystem.model.rms.Station;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @Getter
    @Setter
    @NotNull
    @Size(min = 3, max = 30)
    private String username;


    @Getter
    @Setter
    @NotNull
    @Size(min = 8)
    @Column(name = "user_password", nullable = false)
    private String password;

    @Getter
    @Setter
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Collection<Station> stations;
}
