package com.astellas.poc.sdlc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Table(name = "urs")
@Entity
public class URS {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "document_id")
    private String documentId;

    @OneToMany(mappedBy = "urs", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    private Set<URSDetail> ursDetails;

    @Embedded
    private MetaInfo metaInfo;

    @Column(name = "business_process_description")
    private String businessProcessDescription;

    private String version;

    public URS setUrsDetails(Set<URSDetail> ursDetails) {
        this.ursDetails = ursDetails;
        ursDetails.parallelStream().forEach(ursDetail -> ursDetail.setUrs(this));
        return this;
    }
}
