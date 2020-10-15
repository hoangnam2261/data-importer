package com.astellas.poc.sdlc.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Set;

@Table(name = "test_script")
public class TestScript {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "document_id")
    private String documentId;

    @Version
    private String version;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "test_category")
    private String testCategory;

    private String purpose;

    private String prerequisites;

    @OneToMany(mappedBy = "testScript")
    private Set<TestCase> testCases;
}
