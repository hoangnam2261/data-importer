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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

@Table(name = "urs_detail")
@Setter
@Entity
@Builder
public class URSDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private URS urs;

    //TODO we need to add 1 more field: Related URS "frs_detail.related_urs, urs_frs_relation.*"
    @ManyToMany(mappedBy = "ursDetails")
    private Set<FRSDetail> frsDetails;

    @Column(name = "requirement_category")
    @Enumerated(EnumType.STRING)
    private URSRequirementCategory requirementCategory;

    @Embedded
    private DetailMetaInfo detailMetaInfo;
}
