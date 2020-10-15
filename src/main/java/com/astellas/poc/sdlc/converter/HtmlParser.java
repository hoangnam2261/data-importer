package com.astellas.poc.sdlc.converter;

import com.astellas.poc.sdlc.models.DetailMetaInfo;
import com.astellas.poc.sdlc.models.MetaInfo;
import com.astellas.poc.sdlc.models.Project;
import com.astellas.poc.sdlc.models.URS;
import com.astellas.poc.sdlc.models.URSDetail;
import com.astellas.poc.sdlc.models.URSRequirementCategory;
import com.astellas.poc.sdlc.repository.ProjectRepository;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class HtmlParser implements ApplicationRunner {

    @Autowired
    private ProjectRepository projectRepository;

    public void convert() throws Exception {
        File file = new File("C:\\Users\\LW81343\\Desktop\\base_code1\\data-importer\\testdata\\root_folder\\projectA\\URS-SAMPLE-01234567-v1.0.docx.html");
        Document document = Jsoup.parse(file, "UTF-8");
        String documentType = document.select("*.document_type").first().text();
        String documentId = document.select("*.document_id").first().text();
        String documentVersion = document.select("*.document_ver").first().text();
        Project project = new Project();

        project.setName("ProjectA");
        URS urs = new URS();
        switch (documentType) {
            case "URS":
                Set<URSDetail> ursDetails = parseURSDetail(document);
                urs.setUrsDetails(ursDetails);
            case "FRS":
                String businessProcessDescription = getTextOfAllElement(document, "p.business_process_description");
                urs.setBusinessProcessDescription(businessProcessDescription);
            case "DCS":
                //This is parse metaInfo
                //TODO add fileName
                MetaInfo metaInfo = parseMetaInfo(document);
                urs.setMetaInfo(metaInfo);
                break;
            default:
                break;
        }
        urs.setDocumentId(documentId);
        urs.setVersion(documentVersion);
        project.addUrs(urs);
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
                                     .map(row -> {
                                         String requirementId = row.select("td.urs_id").first().text();
                                         String description = getTextOfAllElement(row, "td.requirement_description");
                                         String riskArea = getTextOfAllElement(row, "td.risk_areabusiness_regulated_safety");
                                         String criticalityRating = getTextOfAllElement(row, "td.criticality_rating_1_2_or_3");
                                         return URSDetail.builder()
                                                         .requirementCategory(requirementCategory)
                                                         .detailMetaInfo(
                                                                 DetailMetaInfo.builder()
                                                                               .requirementId(requirementId)
                                                                               .description(description)
                                                                               .riskArea(riskArea)
                                                                               .criticalityRating(criticalityRating)
                                                                               .build())
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
