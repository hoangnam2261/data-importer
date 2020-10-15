package com.astellas.poc.sdlc.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DetailMetaInfo {
    @Column(name = "requirement_id")
    private String requirementId;

    private String description;

    @Column(name = "riskArea")
    private String riskArea;

    @Column(name = "criticality_rating")
    private String criticalityRating;
}
