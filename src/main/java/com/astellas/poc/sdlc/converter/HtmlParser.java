package com.astellas.poc.sdlc.converter;

import com.astellas.poc.sdlc.cssSelector.CssSelector;
import com.astellas.poc.sdlc.models.AbstractDocument;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class HtmlParser {

    public static final String URS = "URS";
    public static final String FRS = "FRS";
    public static final String DCS = "DCS";
    public static final String TEST_SCRIPT = "Test Script";

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CssSelector cssSel;

    public void convert(String projectName, Collection<File> files) {
        //A body guard: If only have projectFolder without any files in it
        // We DO NOT delete existed project in database and start insert process
        if (files.isEmpty()) {
            return;
        }
        deleteProjectIfExisted(projectName);
        Project project = new Project();
        project.setName(projectName);
        files.parallelStream()
                .forEach(file -> {
                    try {
                        Document document = Jsoup.parse(file, "UTF-8");
                        String documentType = document.selectFirst(cssSel.getDocSel().getDocumentType()).text();
                        switch (documentType) {
                            case URS:
                                parseURS(project, document, file.getName());
                                break;
                            case FRS:
                                parseFRS(project, document, file.getName());
                                break;
                            case DCS:
                                parseDCS(project, document, file.getName());
                                break;
                            case TEST_SCRIPT:
                                parseTestScript(project, document, file.getName());
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        log.error("Error occurred in processing file {} in project {}",
                                file.getName(),
                                projectName,
                                e);
                    }
                });
        populateURS_FRS_Relation(project);
        projectRepository.save(project);
    }

    private void parseTestScript(Project project, Document document, String fileName) {
        String testCategory = document.selectFirst(cssSel.getTestScrSel().getTest_script_type()).text();
        String purpose = document.selectFirst(cssSel.getTestScrSel().getPurpose()).text();
        String prerequisites = getTextOfAllElement(document, cssSel.getTestScrSel().getPrerequisites());
        TestScript testScript = TestScript.builder()
                .testCategory(testCategory)
                .purpose(purpose)
                .prerequisites(prerequisites)
                .build();
        Set<TestCase> testCases = parseTestCase(document);
        testScript.setFileName(fileName);
        testScript.setTestCases(testCases);
        populateAbstractDocument(testScript, document);
        project.addTestScript(testScript);
    }

    private void parseDCS(Project project, Document document, String fileName) {
        DCS dcs = new DCS();
        //Get element after "System Description"
        Element element = document.selectFirst("p.system_description");
        Elements elements = element.nextElementSiblings();
        Set<DCSItem> dcsItems = new HashSet<>();
        elements.parallelStream()
                .forEach(e -> {
                    if (e.tagName().matches("h[1-6]")) {
                        if (!e.select(cssSel.getDcsSel().getItem_no()).isEmpty()) {
                            DCSItem dcsItem = new DCSItem();
                            dcsItem.setItemNumber(e.selectFirst(cssSel.getDcsSel().getItem_no()).text());
                            dcsItem.setItemTitle(e.text());
                            if ("p".equals(e.nextElementSibling().tagName())) {
                                dcsItem.setSpecification(e.nextElementSibling().text());
                            }
                            dcsItems.add(dcsItem);
                        }
                    }
                });
        dcs.setDcsItems(dcsItems);
        dcs.setMetaInfo(parseMetaInfo(document, fileName));
        populateAbstractDocument(dcs, document);
        project.addDcs(dcs);
    }

    private void parseFRS(Project project, Document document, String fileName) {
        Set<FRSDetail> frsDetails = parseFRSDetail(document);
        FRS frs = new FRS();
        frs.setFrsDetails(frsDetails);
        frs.setBusinessProcessDescription(getTextOfAllElement(document, "p.business_process_description"));
        frs.setMetaInfo(parseMetaInfo(document, fileName));
        populateAbstractDocument(frs, document);
        project.addFrs(frs);
    }

    private void parseURS(Project project, Document document, String fileName) {
        Set<URSDetail> ursDetails = parseURSDetail(document);
        URS urs = new URS();
        urs.setUrsDetails(ursDetails);
        urs.setBusinessProcessDescription(getTextOfAllElement(document, cssSel.getUrsSel().getBusiness_process_description()));
        urs.setMetaInfo(parseMetaInfo(document, fileName));
        populateAbstractDocument(urs, document);
        project.addUrs(urs);
    }

    private void deleteProjectIfExisted(String projectName) {
        projectRepository.findProjectByName(projectName)
                         .ifPresent(project -> projectRepository.delete(project));
    }

    private void populateAbstractDocument(AbstractDocument abstractDocument,
                                          Element element) {
        String documentId = element.selectFirst(cssSel.getDocSel().getDocumentId()).text();
        String documentVersion = element.selectFirst(cssSel.getDocSel().getDocumentVersion()).text();
        abstractDocument.setDocumentId(documentId);
        abstractDocument.setVersion(documentVersion);
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
                                     .filter(row -> row.select("th").isEmpty() && !row.select(cssSel.getUrsSel().getUrs_id()).isEmpty()) //Filter tr with header and contain urs_id
                                     .map(row -> URSDetail.builder()
                                                      .requirementCategory(requirementCategory)
                                                      .detailMetaInfo(parseDetailMetaInfo(row, cssSel.getUrsSel().getUrs_id()))
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
                                     .filter(row -> row.select("th").isEmpty() && !row.select(cssSel.getFrsSel().getFrs_id()).isEmpty()) //Filter tr with header and contain urs_id
                                     .map(row -> {
                                         String relatedURS = row.selectFirst(cssSel.getFrsSel().getParent_urs_id_number()).text();
                                         return FRSDetail.builder()
                                                         .relatedURS(relatedURS)
                                                         .requirementCategory(requirementCategory)
                                                         .detailMetaInfo(parseDetailMetaInfo(row, cssSel.getFrsSel().getFrs_id()))
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
        String description = getTextOfAllElement(row, cssSel.getDeMeInSel().getRequirement_description());
        String riskArea = getTextOfAllElement(row, cssSel.getDeMeInSel().getRisk_areabusiness_regulated_safety());
        String criticalityRating = getTextOfAllElement(row, cssSel.getDeMeInSel().getCriticality_rating_1_2_or_3());
        return
                DetailMetaInfo.builder()
                              .requirementId(requirementId)
                              .description(description)
                              .riskArea(riskArea)
                              .criticalityRating(criticalityRating)
                              .build();
    }

    private Set<TestCase> parseTestCase(Element element) {
        return element.select(cssSel.getTestCaSel().getTest_case_div())
                      .parallelStream()
                      .map(div -> {
                          String caseId = div.selectFirst(cssSel.getTestCaSel().getTest_case_id()).text();
                          String title = div.selectFirst(cssSel.getTestCaSel().getTest_case_title()).text();
                          String objective = div.selectFirst(cssSel.getTestCaSel().getTest_objective()).text();
                          String requirement = div.selectFirst(cssSel.getTestCaSel().getTested_requirements()).text();
                          String acceptanceCriteria = div.selectFirst(cssSel.getTestCaSel().getTest_acceptance_criteria()).text();
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
                      .filter(row -> !row.select(cssSel.getScriptInsSel().getStep()).isEmpty())
                      .map(row -> {
                          String stepNumber = row.selectFirst(cssSel.getScriptInsSel().getStep()).text();
                          String instruction = row.selectFirst(cssSel.getScriptInsSel().getStep_instruction()).text();
                          String expectedResults = row.selectFirst(cssSel.getScriptInsSel().getExpected_results()).text();
                          String requirement = getTextOfAllElement(row, cssSel.getScriptInsSel().getTested_requirements());
                          return ScriptInstruction.builder()
                                                  .stepNumber(stepNumber)
                                                  .stepInstruction(instruction)
                                                  .expectedResults(expectedResults)
                                                  .testedRequirements(requirement)
                                                  .build();
                      }).collect(Collectors.toSet());
    }

    private MetaInfo parseMetaInfo(Element element, String fileName) {
        //This is parse metaInfo
        String purpose = getTextOfAllElement(element, cssSel.getMetaISel().getPurpose());
        String scope = getTextOfAllElement(element, cssSel.getMetaISel().getScope());
        String outOfScope = getTextOfAllElement(element, cssSel.getMetaISel().getOut_of_scope());
        String assumptions = getTextOfAllElement(element, cssSel.getMetaISel().getAssumptions());
        String limitations = getTextOfAllElement(element, cssSel.getMetaISel().getLimitations());
        String dependencies = getTextOfAllElement(element, cssSel.getMetaISel().getDependencies());
        String systemDescription = getTextOfAllElement(element, cssSel.getMetaISel().getSystem_description());
        return MetaInfo.builder()
                       .purpose(purpose)
                       .scope(scope)
                       .outOfScope(outOfScope)
                       .assumptions(assumptions)
                       .limitations(limitations)
                       .dependencies(dependencies)
                       .systemDescription(systemDescription)
                       .fileName(fileName)
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

    private void populateURS_FRS_Relation(Project project) {
        project.getFrs()
                .parallelStream()
                .forEach(frs -> {
                    frs.getFrsDetails()
                            .parallelStream()
                            .forEach(frsDetail -> {
                                Stream.of(frsDetail.getRelatedURS().split(StringUtils.SPACE))
                                        .filter(StringUtils::isNoneBlank)
                                        .forEach(ursDeRe -> {
                                            project.getUrs()
                                                    .parallelStream()
                                                    .forEach(urs -> {
                                                        urs.getUrsDetails()
                                                                .parallelStream()
                                                                .filter(ursDetail -> StringUtils.equals(ursDetail.getDetailMetaInfo().getRequirementId(), ursDeRe))
                                                                .forEach(ursDetail -> ursDetail.addFrsDetail(frsDetail));
                                                    });
                                        });
                            });
                });
    }
}
