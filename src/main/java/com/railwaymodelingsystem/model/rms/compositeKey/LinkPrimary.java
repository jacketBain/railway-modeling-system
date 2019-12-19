package com.railwaymodelingsystem.model.rms.compositeKey;

import com.railwaymodelingsystem.model.rms.Link;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode
@ToString
@Embeddable
@IdClass(Link.class)
public class LinkPrimary implements Serializable {
    @Getter
    @Setter
    @Column(name = "link_block_from")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer blockFromId;

    @Getter
    @Setter
    @Column(name = "link_block_to")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer blockToId;
}
