package com.astellas.poc.sdlc.models;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

@Builder
@Table(name = "frs_detail")
public class FRSDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private FRS frs;

    @Column(name = "requirement_category")
    @Enumerated(EnumType.STRING)
    private FRSRequirementCategory requirementCategory;

    @Column(name = "requirement_id")
    private Long requirementId;

    @ManyToMany
    private Set<URSDetail> ursDetails;

    @Embedded
    private DetailMetaInfo detailMetaInfo;
}
