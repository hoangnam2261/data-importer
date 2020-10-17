package com.astellas.poc.sdlc.models;

import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Setter
@Table(name = "dcs")
@Entity
public class DCS extends AbstractDocument {

    @OneToMany(mappedBy = "dcs", cascade = CascadeType.ALL)
    private Set<DCSItem> dcsItems;

    @Embedded
    private MetaInfo metaInfo;

    public DCS setDcsItems(Set<DCSItem> dcsItems) {
        this.dcsItems = dcsItems;
        dcsItems.parallelStream().forEach(dcsItem -> dcsItem.setDcs(this));
        return this;
    }
}
