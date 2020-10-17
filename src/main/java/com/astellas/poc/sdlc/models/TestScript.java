package com.astellas.poc.sdlc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Table(name = "test_script")
@Entity
public class TestScript extends AbstractDocument {

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "test_category")
    private String testCategory;

    private String purpose;

    private String prerequisites;

    @OneToMany(mappedBy = "testScript", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TestCase> testCases;

    public TestScript setTestCases(Set<TestCase> testCases) {
        this.testCases = testCases;
        testCases.parallelStream().forEach(testCase -> testCase.setTestScript(this));
        return this;
    }
}
