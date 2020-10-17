package com.astellas.poc.sdlc.models;

import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Setter
@MappedSuperclass
public class AbstractDocument extends Auditable {

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "version")
    private String version;
}
