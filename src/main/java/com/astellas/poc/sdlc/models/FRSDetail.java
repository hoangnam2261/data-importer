package com.astellas.poc.sdlc.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    @Builder.Default
    @ManyToMany(mappedBy = "frsDetails", fetch=FetchType.EAGER)
    private Set<URSDetail> ursDetails = new HashSet<>();

    @Embedded
    private DetailMetaInfo detailMetaInfo;

    @Column(name = "related_urs")
    private String relatedURS;

    public void addUrsDetail(URSDetail ursDetail) {
        this.ursDetails.add(ursDetail);
    }
}
