package com.astellas.poc.sdlc.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "dsc_item")
@Entity
public class DCSItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DCS dcs;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "item_title")
    private String itemTitle;

    private String specification;
}
