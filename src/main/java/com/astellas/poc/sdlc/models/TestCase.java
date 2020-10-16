package com.astellas.poc.sdlc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "test_case")
@Entity
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private TestScript testScript;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL)
    private Set<ScriptInstruction> scriptInstructions = new HashSet<>();

    @Column(name = "test_case_id")
    private String testCaseId;

    @Column(name = "test_case_title")
    private String testCaseTitle;

    @Column(name = "test_objective")
    private String testObjective;

    @Column(name = "tested_requirements")
    private String testedRequirements;

    @Column(name = "test_acceptance_criteria")
    private String testAcceptanceCriteria;

    public TestCase setScriptInstructions(Set<ScriptInstruction> scriptInstructions) {
        this.scriptInstructions = scriptInstructions;
        scriptInstructions.parallelStream().forEach(scriptInstruction -> scriptInstruction.setTestCase(this));
        return this;
    }
}
