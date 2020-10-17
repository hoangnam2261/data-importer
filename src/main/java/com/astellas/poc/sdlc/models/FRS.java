package com.astellas.poc.sdlc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Setter
@Getter
@Table(name = "frs")
@Entity
public class FRS extends AbstractDocument {

    @OneToMany(mappedBy = "frs", fetch= FetchType.EAGER)
    private Set<FRSDetail> frsDetails;

    @Embedded
    private MetaInfo metaInfo;

    @Column(name = "business_process_description")
    private String businessProcessDescription;

    public FRS setFrsDetails(Set<FRSDetail> frsDetails) {
        this.frsDetails = frsDetails;
        frsDetails.parallelStream().forEach(fd -> fd.setFrs(this));
        return this;
    }
}
