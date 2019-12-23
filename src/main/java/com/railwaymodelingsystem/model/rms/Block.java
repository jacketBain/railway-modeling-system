package com.railwaymodelingsystem.model.rms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Table(name = "Block", schema = "public")
public class Block {
    @Id
    @Getter
    @Column(name = "block_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @NotNull
    @Column(name = "block_name", nullable = false)
    private String name;

    @Getter
    @Setter
    @NotNull
    @Column(name = "block_length", nullable = false)
    private Integer length;

    @Getter
    @Setter
    @NotNull
    @Column(name = "block_has_platform")
    private Boolean hasPlatform;

    @Getter
    @Setter
    @Nullable
    @Column(name = "block_platform_number")
    private Integer platformNumber;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional=false, cascade=CascadeType.MERGE)
    @JoinColumn(name="block_way", foreignKey = @ForeignKey(name = "FK_BlockWay"))
    private Way way;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(optional=false)
    @JoinColumn(name="block_station", foreignKey = @ForeignKey(name = "FK_BlockStation"))
    private Station station;
}
