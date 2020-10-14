package com.astellas.poc.sdlc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Constants {

  // --------------------------------
  // Common
  // --------------------------------
  // generate class name
  public static List<String> REPLACE_TO_UNDERBAR = new ArrayList<>(Arrays.asList(" ", "　"));
  public static List<String> REPLACE_TO_BLANK = new ArrayList<>(Arrays.asList("☐"));
  // header const
  public static String CONST_DOCUMENT_ID = "Document ID";
  public static String CONST_DOCUMENT_VERSION = "Document Version";

  // --------------------------------
  // TestScript
  // --------------------------------
  // Test Case Details const
  public static String PARA_TITLE_TEST_CASE_DETAILS = "Test Case Details";
  public static String PARA_TITLE_ENVIRONMENT = "Environment";
  // Test Script File regex
  private static String IVFILE_PREFIX = "^IV(\\d*)*-.*";
  private static String OVFILE_PREFIX = "^OV(\\d*)*-.*";
  private static String OPVFILE_PREFIX = "^OPV(\\d*)*-.*";
  private static String PVFILE_PREFIX = "^PV(\\d*)*-.*";
  private static String DLFILE_PREFIX = "^DL(\\d*)*-.*";
  public static List<String> TEST_SCRIPT_FILE_PREFIX_LIST = new ArrayList<>(
      Arrays.asList(IVFILE_PREFIX, OVFILE_PREFIX, OPVFILE_PREFIX, PVFILE_PREFIX, DLFILE_PREFIX));

  // --------------------------------
  // URS/FRS
  // --------------------------------
  // Class map for the Requirement Category
  @SuppressWarnings("serial")
  public static Map<String, String> REQUIREMENT_CATEGORY_CLASS_MAP = new HashMap<String, String>() {
    {
      put("General User Requirements", "general_user_requirements");
      put("Operations Requirements", "operations_requirements");
      put("User Interface Requirements", "user_interface_requirements");
      put("User Interface", "user_interface_requirements"); // for error in writing(FRS)
      put("Input / Output Requirements", "input_output_requirements");
      put("System Interface Requirements", "system_interface_requirements");
      put("Data Requirements", "data_requirements");
      put("Reporting Requirements", "reporting_requirements");
      put("Hardware and Infrastructure Requirements", "hardware_and_infrastructure_requirements");
      put("User Access / Roles Requirements", "user_access_roles_requirements");
      put("Security Requirements", "security_requirements");
      put("Security", "security_requirements"); // for error in writing(FRS)
      put("Business Continuity Plan", "business_continuity_plan");
      put("Business Continuity", "business_continuity"); // major item
      put("System Recovery", "system_recovery");
      put("Back-up", "back_up");
      put("Performance", "performance");
      put("Electronic Records / Electronic Signatures Regulatory Requirements",
          "electronic_records_electronic_signatures_regulatory_requirements"); // major item
      put("Regulatory Requirements", "regulatory_requirements");
      put("Electronic Records General", "electronic_records_general");
      put("Electronic Signatures", "electronic_signatures");
      put("Signature / Record Linking", "signature_record_linking");
      put("Electronic Signature Components and Controls", "electronic_signature_components_and_controls");
      put("Audit Trails", "audit_trails");
      put("Data Privacy and Protection Requirements", "data_privacy_and_protection_requirements");
      put("Japanese Sarbanes Oxley (J-SOX) Requirements", "japanese_sarbanes_oxley_j_sox_requirements");
      put("Support and maintenance requirements", "support_and_maintenance_requirements");
      put("Non-computerized / Non-functional requirements", "non_computerized_non_functional_requirements");
      put("Deferred requirements", "deferred_requirements");
    }
  };

}
