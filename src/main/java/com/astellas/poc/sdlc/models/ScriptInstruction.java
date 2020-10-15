package com.astellas.poc.sdlc.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "script_instruction")
public class ScriptInstruction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private TestCase testCase;

    @Column(name = "step_number")
    private String stepNumber;

    @Column(name = "step_instruction")
    private String stepInstruction;

    @Column(name = "expected_results")
    private String expectedResults;

    @Column(name = "tested_requirements")
    private String testedRequirements;
}
