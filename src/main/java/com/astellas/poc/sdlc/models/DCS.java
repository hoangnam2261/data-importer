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

@Table(name = "dcs")
public class DCS {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "document_id")
    private String documentId;

    @OneToMany(mappedBy = "dcs")
    private Set<DCSItem> dcsItems;

    @Embedded
    private MetaInfo metaInfo;

    @Version
    private String version;
}
