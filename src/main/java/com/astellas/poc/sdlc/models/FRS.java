package com.astellas.poc.sdlc.models;

import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Setter
@Table(name = "frs")
@Entity
public class FRS {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "document_id")
    private String documentId;

    @OneToMany(mappedBy = "frs", cascade = CascadeType.ALL)
    private Set<FRSDetail> frsDetails;

    @Embedded
    private MetaInfo metaInfo;

    @Column(name = "business_process_description")
    private String businessProcessDescription;

    private String version;

    public FRS setFrsDetails(Set<FRSDetail> frsDetails) {
        this.frsDetails = frsDetails;
        frsDetails.parallelStream().forEach(fd -> fd.setFrs(this));
        return this;
    }
}
