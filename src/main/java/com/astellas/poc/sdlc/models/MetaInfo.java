package com.astellas.poc.sdlc.models;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Builder
public class MetaInfo {

    @Column(name = "file_name")
    private String fileName;

    private String purpose;

    private String scope;

    @Column(name = "out_of_scope")
    private String outOfScope;

    private String assumptions;

    private String limitations;

    private String dependencies;

    @Column(name = "system_description")
    private String systemDescription;
}
