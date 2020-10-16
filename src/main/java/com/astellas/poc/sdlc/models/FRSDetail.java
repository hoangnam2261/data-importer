package com.astellas.poc.sdlc.models;

import lombok.Builder;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

@Builder
@Setter
@Table(name = "frs_detail")
@Entity
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

    @ManyToMany
    @JoinTable(name = "urs_frs_relation",
            joinColumns = @JoinColumn(name = "crs_detail_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "frs_detail_id", referencedColumnName = "id"))
    private Set<URSDetail> ursDetails;

    @Embedded
    private DetailMetaInfo detailMetaInfo;

    @Column(name = "related_urs")
    private String relatedURS;

}
