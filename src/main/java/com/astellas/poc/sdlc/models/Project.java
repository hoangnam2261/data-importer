package com.astellas.poc.sdlc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Table(name = "project")
@Entity
public class Project extends Auditable {

    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<URS> urs = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<FRS> frs = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<DCS> dcs = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<TestScript> testScripts = new HashSet<>();

    public void addUrs(URS urs) {
        this.urs.add(urs);
        urs.setProject(this);
    }

    public void addFrs(FRS frs) {
        this.frs.add(frs);
        frs.setProject(this);
    }

    public void addDcs(DCS dcs) {
        this.dcs.add(dcs);
        dcs.setProject(this);
    }

    public void addTestScript(TestScript testScript) {
        this.testScripts.add(testScript);
        testScript.setProject(this);
    }
}
