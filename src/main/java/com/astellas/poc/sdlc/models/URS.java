package com.astellas.poc.sdlc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Setter
@Getter
@Table(name = "urs")
@Entity
public class URS extends AbstractDocument {

    @OneToMany(mappedBy = "urs", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    private Set<URSDetail> ursDetails;

    @Embedded
    private MetaInfo metaInfo;

    @Column(name = "business_process_description")
    private String businessProcessDescription;

    public URS setUrsDetails(Set<URSDetail> ursDetails) {
        this.ursDetails = ursDetails;
        ursDetails.parallelStream().forEach(ursDetail -> ursDetail.setUrs(this));
        return this;
    }
}
