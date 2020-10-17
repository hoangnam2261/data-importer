package com.astellas.poc.sdlc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Table(name = "script_instruction")
@Entity
public class ScriptInstruction extends Auditable {

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
