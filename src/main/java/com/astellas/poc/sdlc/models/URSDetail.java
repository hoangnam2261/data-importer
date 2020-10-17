package com.astellas.poc.sdlc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "urs_detail")
@Entity
public class URSDetail extends Auditable {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private URS urs;

    //TODO we need to add 1 more field: Related URS "frs_detail.related_urs, urs_frs_relation.*"
    @Builder.Default
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "urs_frs_relation",
            joinColumns = @JoinColumn(name = "urs_detail_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "frs_detail_id", referencedColumnName = "id"))
    private Set<FRSDetail> frsDetails = new HashSet<>();

    @Column(name = "requirement_category")
    @Enumerated(EnumType.STRING)
    private URSRequirementCategory requirementCategory;

    @Embedded
    private DetailMetaInfo detailMetaInfo;

    public void addFrsDetail(FRSDetail frsDetail) {
        this.frsDetails.add(frsDetail);
        frsDetail.addUrsDetail(this);
    }
}
