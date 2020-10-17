package com.astellas.poc.sdlc.cssSelector;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:/css-selector.yml")
@Configuration
@Getter
@Setter
public class CssSelector {

    @Autowired
    private DocumentSelector docSel;

    @Autowired
    private MetaInfoSelector metaISel;

    @Autowired
    private URSSelector ursSel;

    @Autowired
    private FRSSelector frsSel;

    @Autowired
    private DetailMetaInfoSelector deMeInSel;

    @Autowired
    private DCSSelector dcsSel;

    @Autowired
    private TestScriptSelector testScrSel;

    @Autowired
    private TestCaseSelector testCaSel;

    @Autowired
    private ScriptInstructionSelector scriptInsSel;

    @Bean
    @ConfigurationProperties(prefix = "document")
    public DocumentSelector documentSelector() {
        return new DocumentSelector();
    }

    @Bean
    @ConfigurationProperties(prefix = "meta.info")
    public MetaInfoSelector metaInfoSelector() {
        return new MetaInfoSelector();
    }

    @Bean
    @ConfigurationProperties(prefix = "urs")
    public URSSelector ursSelector() {
        return new URSSelector();
    }

    @Bean
    @ConfigurationProperties(prefix = "frs")
    public FRSSelector frsSelector() {
        return new FRSSelector();
    }

    @Bean
    @ConfigurationProperties(prefix = "detail.meta.info")
    public DetailMetaInfoSelector detailMetaInfoSelector() {
        return new DetailMetaInfoSelector();
    }

    @Bean
    @ConfigurationProperties(prefix = "dcs")
    public DCSSelector dcsSelector() {
        return new DCSSelector();
    }

    @Bean
    @ConfigurationProperties(prefix = "testscript")
    public TestScriptSelector testScriptSelector() {
        return new TestScriptSelector();
    }

    @Bean
    @ConfigurationProperties(prefix = "testcase")
    public TestCaseSelector testCaseSelector() {
        return new TestCaseSelector();
    }

    @Bean
    @ConfigurationProperties(prefix = "scriptinstruction")
    public ScriptInstructionSelector scriptInstructionSelector() {
        return new ScriptInstructionSelector();
    }
}
