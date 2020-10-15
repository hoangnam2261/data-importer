package com.astellas.poc.sdlc.models;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum FRSRequirementCategory {
    GENERAL_FUNCTIONAL_REQUIREMENTS,
    OPERATIONS,
    USER_INTERFACE_REQUIREMENTS,
    INPUT_OUTPUT_REQUIREMENTS,
    SYSTEM_INTERFACE_REQUIREMENTS,
    DATA_REQUIREMENTS,
    REPORTING,
    HARDWARE_AND_INFRASTRUCTURE,
    USER_ACCESS_ROLES_REQUIREMENTS,
    SECURITY_REQUIREMENTS,
    SYSTEM_RECOVERY,
    BACK_UP,
    PERFORMANCE,
    REGULATORY_REQUIREMENTS,
    ELECTRONIC_RECORDS_ELECTRONIC_SIGNATURES_REGULATORY_REQUIREMENTS,
    DATA_PRIVACY_AND_PROTECTION_REQUIREMENTS,
    JAPANESE_SARBANES_OXLEY_J_SOX_REQUIREMENTS,
    SUPPORT_AND_MAINTENANCE_REQUIREMENTS;

    public static Set<String> getFRSRequirementCategoryClasses() {
        return Stream.of(FRSRequirementCategory.values())
                     .map(FRSRequirementCategory -> FRSRequirementCategory.name().toLowerCase())
                     .collect(Collectors.toSet());
    }
}
