package com.astellas.poc.sdlc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailMetaInfo {
    @Column(name = "requirement_id")
    private String requirementId;

    private String description;

    @Column(name = "riskArea")
    private String riskArea;

    @Column(name = "criticality_rating")
    private String criticalityRating;
}
