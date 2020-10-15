package com.astellas.poc.sdlc.models;

import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Table(name = "project")
@Entity
@Setter
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<URS> urs = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<FRS> frs = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<DCS> dcs = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<TestScript> testScripts = new HashSet<>();

    public void addUrs(URS urs) {
        this.urs.add(urs);
        urs.setProject(this);
    }
}
