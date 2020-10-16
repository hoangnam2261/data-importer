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
@Table(name = "dcs")
@Entity
public class DCS {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "document_id")
    private String documentId;

    @OneToMany(mappedBy = "dcs", cascade = CascadeType.ALL)
    private Set<DCSItem> dcsItems;

    @Embedded
    private MetaInfo metaInfo;

    private String version;

    public DCS setDcsItems(Set<DCSItem> dcsItems) {
        this.dcsItems = dcsItems;
        dcsItems.parallelStream().forEach(dcsItem -> dcsItem.setDcs(this));
        return this;
    }
}
