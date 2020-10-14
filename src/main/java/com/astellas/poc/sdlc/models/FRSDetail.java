package com.astellas.poc.sdlc.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class FRSDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private FRS frs;

    @Column(name = "requirement_category")
    private String requirementCategory;

    @Column(name = "requirement_id")
    private Long requirementId;

}
