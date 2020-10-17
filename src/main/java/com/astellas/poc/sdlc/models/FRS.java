package com.astellas.poc.sdlc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
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

    @OneToMany(mappedBy = "frs", fetch= FetchType.EAGER)
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
