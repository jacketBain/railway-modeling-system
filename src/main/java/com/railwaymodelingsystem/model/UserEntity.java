package com.railwaymodelingsystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users", schema = "public")
public class UserEntity {
    @Id
    @NotNull
    @Size(min = 3, max = 30)
    @Getter
    @Setter
    private String username;

    @Column(nullable = false)
    @Getter
    @Setter
    @NotNull
    @Size(min = 8)
    private String password;
}
