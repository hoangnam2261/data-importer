package com.astellas.poc.sdlc.cssSelector;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MetaInfoSelector {
    private String purpose;
    private String scope;
    private String out_of_scope;
    private String assumptions;
    private String limitations;
    private String dependencies;
    private String system_description;
}
