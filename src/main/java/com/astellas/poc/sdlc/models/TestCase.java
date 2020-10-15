package com.astellas.poc.sdlc.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "test_case")
@Entity
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private TestScript testScript;

    @Column(name = "test_case_id")
    private String testCaseId;

    @Column(name = "test_case_title")
    private String testCaseTitle;

    @Column(name = "test_objective")
    private String testObjective;

    @Column(name = "test_requirements")
    private String testRequirements;

    @Column(name = "test_acceptance_criteria")
    private String testAcceptanceCriteria;
}
