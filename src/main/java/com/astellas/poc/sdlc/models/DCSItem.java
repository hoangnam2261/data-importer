package com.astellas.poc.sdlc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Table(name = "dcs_item")
@Entity
public class DCSItem extends Auditable {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DCS dcs;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "item_title")
    private String itemTitle;

    private String specification;
}
