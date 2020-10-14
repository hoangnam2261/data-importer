package com.astellas.poc.sdlc.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "system_name")
    private String systemName;

    @OneToMany(mappedBy = "project")
    private Set<URS> urs;

    @OneToMany(mappedBy = "project")
    private Set<FRS> frs;

    @OneToMany(mappedBy = "project")
    private Set<DCS> dcs;
}
