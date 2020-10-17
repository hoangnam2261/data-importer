package com.astellas.poc.sdlc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "frs_detail")
@Entity
public class FRSDetail extends Auditable {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private FRS frs;

    @Column(name = "requirement_category")
    @Enumerated(EnumType.STRING)
    private FRSRequirementCategory requirementCategory;

    @Builder.Default
    @ManyToMany(mappedBy = "frsDetails")
    private Set<URSDetail> ursDetails = new HashSet<>();

    @Embedded
    private DetailMetaInfo detailMetaInfo;

    @Column(name = "related_urs")
    private String relatedURS;

    public void addUrsDetail(URSDetail ursDetail) {
        this.ursDetails.add(ursDetail);
    }
}
