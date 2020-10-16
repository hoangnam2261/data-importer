package com.astellas.poc.sdlc.converter;

import com.astellas.poc.sdlc.models.DCS;
import com.astellas.poc.sdlc.models.DCSItem;
import com.astellas.poc.sdlc.models.DetailMetaInfo;
import com.astellas.poc.sdlc.models.FRS;
import com.astellas.poc.sdlc.models.FRSDetail;
import com.astellas.poc.sdlc.models.FRSRequirementCategory;
import com.astellas.poc.sdlc.models.MetaInfo;
import com.astellas.poc.sdlc.models.Project;
import com.astellas.poc.sdlc.models.ScriptInstruction;
import com.astellas.poc.sdlc.models.TestCase;
import com.astellas.poc.sdlc.models.TestScript;
import com.astellas.poc.sdlc.models.URS;
import com.astellas.poc.sdlc.models.URSDetail;
import com.astellas.poc.sdlc.models.URSRequirementCategory;
import com.astellas.poc.sdlc.repository.ProjectRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class HtmlParser implements ApplicationRunner {

    @Autowired
    private ProjectRepository projectRepository;

    public void convert() throws Exception {
        Project project = new Project();
        project.setName("ProjectC");
        File dir = new File("C:\\Users\\LW81343\\Desktop\\base_code1\\data-importer\\testdata\\root_folder\\projectA");
        FileUtils.listFiles(dir, new String[]{"html"}, false)
                 .parallelStream()
                 .forEach(file -> {

                     Document document = null;
                     try {
                         document = Jsoup.parse(file, "UTF-8");
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     String documentType = document.selectFirst("*.document_type").text();
                     String documentId = document.selectFirst("*.document_id").text();
                     String documentVersion = document.selectFirst("*.document_ver").text();
                     //This is parse metaInfo
                     //TODO add fileName
                     MetaInfo metaInfo = parseMetaInfo(document);
                     switch (documentType) {
                         case "URS":
                             Set<URSDetail> ursDetails = parseURSDetail(document);
                             URS urs = new URS();
                             urs.setMetaInfo(metaInfo);
                             urs.setUrsDetails(ursDetails);
                             urs.setBusinessProcessDescription(getTextOfAllElement(document, "p.business_process_description"));
                             urs.setDocumentId(documentId);
                             urs.setVersion(documentVersion);
                             project.addUrs(urs);
                             break;
                         case "FRS":
                             FRS frs = new FRS();
                             frs.setMetaInfo(metaInfo);
                             frs.setBusinessProcessDescription(getTextOfAllElement(document, "p.business_process_description"));
                             Set<FRSDetail> frsDetails = parseFRSDetail(document);
                             frs.setFrsDetails(frsDetails);
                             frs.setDocumentId(documentId);
                             frs.setVersion(documentVersion);
                             project.addFrs(frs);
                             break;
                         case "DCS":
                             DCS dcs = new DCS();
                             dcs.setMetaInfo(metaInfo);
                             //Get element after "System Description"
                             Element element = document.selectFirst("p.system_description");
                             Elements elements = element.nextElementSiblings();
                             Set<DCSItem> dcsItems = new HashSet<>();
                             elements.parallelStream()
                                     .forEach(e -> {
                                         //TODO should be ignore Appendix as require
                                         //https://apps.topcoder.com/forums/?module=Thread&threadID=963350&start=0
                                         if (e.tagName().matches("h[1-6]")) {
                                             if (!e.select("span.item_no").isEmpty()) {
                                                 DCSItem dcsItem = new DCSItem();
                                                 dcsItem.setItemNumber(e.selectFirst("span.item_no").text());
                                                 dcsItem.setItemTitle(e.text());
                                                 if ("p".equals(e.nextElementSibling().tagName())) {
                                                     dcsItem.setSpecification(e.nextElementSibling().text());
                                                 }
                                                 dcsItems.add(dcsItem);
                                             }
                                         }
                                     });
                             dcs.setDcsItems(dcsItems);
                             dcs.setDocumentId(documentId);
                             dcs.setVersion(documentVersion);
                             project.addDcs(dcs);
                             break;
                         case "Test Script":
                             String testCategory = document.selectFirst("p.test_script_type").text();
                             String purpose = document.selectFirst("p.purpose").text();
                             String prerequisites = getTextOfAllElement(document,"p.prerequisites");
                             TestScript testScript = TestScript.builder()
                                                               .testCategory(testCategory)
                                                               .purpose(purpose)
                                                               .prerequisites(prerequisites)
                                                               .build();
                             Set<TestCase> testCases = parseTestCase(document);
                             testScript.setTestCases(testCases);
                             testScript.setDocumentId(documentId);
                             testScript.setVersion(documentVersion);
                             project.addTestScript(testScript);
                             break;
                         default:
                             break;
                     }
                 });


        projectRepository.save(project);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        convert();
    }

    private Set<URSDetail> parseURSDetail(Document document) {
        Elements allTables = document.select("table");
        Set<String> requirementCategoryClasses = URSRequirementCategory.getURSRequirementCategoryClasses();
        return allTables.stream()
                        .map(table -> {
                            Collection<String> classes = CollectionUtils.intersection(requirementCategoryClasses, table.classNames());
                            if (!classes.isEmpty()) {
                                URSRequirementCategory requirementCategory = URSRequirementCategory.valueOf(classes.toArray()[0].toString().toUpperCase());
                                return table.select("tr")
                                     .stream()
                                     .filter(row -> row.select("th").isEmpty() && !row.select("td.urs_id").isEmpty()) //Filter tr with header and contain urs_id
                                     .map(row -> URSDetail.builder()
                                                      .requirementCategory(requirementCategory)
                                                      .detailMetaInfo(parseDetailMetaInfo(row, "td.urs_id"))
                                                      .build())
                                     .collect(Collectors.toSet());
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
    }

    private Set<FRSDetail> parseFRSDetail(Document document) {
        Elements allTables = document.select("table");
        Set<String> requirementCategoryClasses = FRSRequirementCategory.getFRSRequirementCategoryClasses();
        return allTables.stream()
                        .map(table -> {
                            Collection<String> classes = CollectionUtils.intersection(requirementCategoryClasses, table.classNames());
                            if (!classes.isEmpty()) {
                                FRSRequirementCategory requirementCategory = FRSRequirementCategory.valueOf(classes.toArray()[0].toString().toUpperCase());
                                return table.select("tr")
                                     .stream()
                                     .filter(row -> row.select("th").isEmpty() && !row.select("td.frs_idnumber").isEmpty()) //Filter tr with header and contain urs_id
                                     .map(row -> {
                                         String relatedURS = row.selectFirst("td.parent_urs_id_number").text();
                                         return FRSDetail.builder()
                                                         .relatedURS(relatedURS)
                                                         .requirementCategory(requirementCategory)
                                                         .detailMetaInfo(parseDetailMetaInfo(row, "td.frs_idnumber"))
                                                         .build();
                                     })
                                     .collect(Collectors.toSet());
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
    }

    private DetailMetaInfo parseDetailMetaInfo(Element row, String requirementIdClass) {
        String requirementId = row.selectFirst(requirementIdClass).text();
        String description = getTextOfAllElement(row, "td.requirement_description");
        String riskArea = getTextOfAllElement(row, "td.risk_areabusiness_regulated_safety");
        String criticalityRating = getTextOfAllElement(row, "td.criticality_rating_1_2_or_3");
        return
                DetailMetaInfo.builder()
                              .requirementId(requirementId)
                              .description(description)
                              .riskArea(riskArea)
                              .criticalityRating(criticalityRating)
                              .build();
    }

    private Set<TestCase> parseTestCase(Element element) {
        return element.select("div.test_case_div")
                      .parallelStream()
                      .map(div -> {
                          String caseId = div.selectFirst("h2.test_case_id").text();
                          String title = div.selectFirst("h2.test_case_title").text();
                          String objective = div.selectFirst("p.test_objective").text();
                          String requirement = div.selectFirst("p.tested_requirements").text();
                          String acceptanceCriteria = div.selectFirst("p.test_acceptance_criteria").text();
                          return TestCase.builder()
                                         .testCaseId(caseId)
                                         .testCaseTitle(title)
                                         .testObjective(objective)
                                         .testedRequirements(requirement)
                                         .testAcceptanceCriteria(acceptanceCriteria)
                                         .build()
                                         .setScriptInstructions(parseScriptInstruction(div));
                      })
                      .collect(Collectors.toSet());
    }

    private Set<ScriptInstruction> parseScriptInstruction(Element element) {
        return element.selectFirst("table.script_instructions")
                      .select("tr")
                      .parallelStream()
                      .filter(row -> !row.select("td.step").isEmpty())
                      .map(row -> {
                          String stepNumber = row.selectFirst("td.step").text();
                          String instruction = row.selectFirst("td.step_instruction").text();
                          String expectedResults = row.selectFirst("td.expected_results").text();
                          return ScriptInstruction.builder()
                                                  .stepNumber(stepNumber)
                                                  .stepInstruction(instruction)
                                                  .expectedResults(expectedResults)
                                                  .build();
                      }).collect(Collectors.toSet());
    }

    private MetaInfo parseMetaInfo(Element element) {
        //This is parse metaInfo
        //TODO add fileName
        String purpose = getTextOfAllElement(element, "p.purpose");
        String scope = getTextOfAllElement(element, "p.scope");
        String outOfScope = getTextOfAllElement(element, "p.out_of_scope");
        String assumptions = getTextOfAllElement(element, "p.assumptions");
        String limitations = getTextOfAllElement(element, "p.limitations");
        String dependencies = getTextOfAllElement(element, "p.dependencies");
        String systemDescription = getTextOfAllElement(element, "p.system_description");
        return MetaInfo.builder()
                       .purpose(purpose)
                       .scope(scope)
                       .outOfScope(outOfScope)
                       .assumptions(assumptions)
                       .limitations(limitations)
                       .dependencies(dependencies)
                       .systemDescription(systemDescription)
                       .build();
    }

    private String getTextOfAllElement(Element element, String cssQuery) {
        Elements select = element.select(cssQuery);
        if (select == null) {
            return StringUtils.EMPTY;
        }
        return select.stream()
                     .map(Element::text)
                     .filter(StringUtils::isNotBlank)
                     .collect(Collectors.joining("\n"));
    }
}
