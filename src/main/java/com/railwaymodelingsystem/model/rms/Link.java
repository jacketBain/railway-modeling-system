package com.railwaymodelingsystem.model.rms;

import com.railwaymodelingsystem.model.rms.compositeKey.LinkPrimary;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Link", schema = "public")
public class Link implements Serializable {
    @Getter
    @Setter
    @EmbeddedId
    private LinkPrimary key;

    @Getter
    @Setter
    @NotNull
    @MapsId("link_block_from")
    @ManyToOne(optional=false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "link_block_from", nullable = false, referencedColumnName = "block_id", foreignKey = @ForeignKey(name = "FK_BlockFrom"))
    private Block blockFrom;

    @Getter
    @Setter
    @NotNull
    @MapsId("link_block_to")
    @ManyToOne(optional=false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "link_block_to", nullable = false, referencedColumnName = "block_id", foreignKey = @ForeignKey(name = "FK_BlockTo"))
    private Block blockTo;
}
