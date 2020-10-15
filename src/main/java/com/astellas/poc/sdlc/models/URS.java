package com.astellas.poc.sdlc.models;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Set;

@Table(name = "urs")
public class URS {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "document_id")
    private String documentId;

    @OneToMany(mappedBy = "urs")
    private Set<URSDetail> ursDetails;

    @Embedded
    private MetaInfo metaInfo;

    @Column(name = "business_process_description")
    private String businessProcessDescription;

    @Version
    private String version;
}
