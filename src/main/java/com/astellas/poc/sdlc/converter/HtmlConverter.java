package com.astellas.poc.sdlc.converter;

import static j2html.TagCreator.*;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.astellas.poc.sdlc.util.Constants;

import j2html.Config;
import j2html.tags.ContainerTag;

@Component
public class HtmlConverter {

  private static Logger logger = LoggerFactory.getLogger(HtmlConverter.class);

  // class name
  private final String CLASS_NAME_DOC_TYPE = "document_type";
  private final String CLASS_NAME_DOC_ID = "document_id";
  private final String CLASS_NAME_DOC_VER = "document_ver";
  private final String CLASS_NAME_TEST_CASE_DIV = "test_case_div";
  private final String CLASS_NAME_TEST_CASE_ID = "test_case_id";
  private final String CLASS_NAME_TEST_CASE_TITLE = "test_case_title";
  private final String CLASS_NAME_ITEM_NO = "item_no";
  // regex
  private final String REGEX_NUMBERING = "^(0|[1-9]+\\d*)(\\.\\d+)*?\t.*"; // ex)"1 Purpose"

  static boolean debug = false;

  public void convert(InputStream in, Writer writer, String fileName) throws Exception {
    if (in == null) {
      throw new IllegalArgumentException("InputStream is requreid.");
    }
    try (XWPFDocument wordDoc = new XWPFDocument(in)) {
      convert(wordDoc, writer, fileName);
    } catch (Exception e) {
      throw e; // TODO
    }
  }

  protected void convert(XWPFDocument doc, Writer writer, String fileName) throws Exception {
    if (doc == null) {
      throw new IllegalArgumentException("doc is requreid.");
    }
    if (writer == null) {
      logger.info("No writer is specified. Using the standard out.");
      writer = new OutputStreamWriter(System.out, "UTF-8");
    }

    String htmlText = toHTML(doc);
    writer.write(htmlText);
    writer.flush();
  }

  public String toHTML(XWPFDocument docx) {

    Config.closeEmptyTags = true;
    ContainerTag html = html(head(link().attr("rel", "stylesheet")));
    ContainerTag body = body();
    html.with(body);

    // header(DocumentID, DocumentVer)
    toHtmlFromHeader(docx, body);
    // body
    toHtmlFromBody(docx, body);

    return html.renderFormatted();
  }

  /**
   * Convert document headers to HTML.
   * 
   * @param docx
   * @param body
   */
  private void toHtmlFromHeader(XWPFDocument docx, ContainerTag body) {
    // ------------------------
    // header(DocumentID, DocumentVer)
    // ------------------------
    int i = 0;
    Boolean setHeader = false;
    for (XWPFHeader header : docx.getHeaderList()) {
      for (IBodyElement b : header.getBodyElements()) {
        if (b.getElementType() == BodyElementType.PARAGRAPH) {
          XWPFParagraph p = (XWPFParagraph) b;
          if (p.getText() == null || p.getText().trim().length() == 0) {
            continue;
          }
          if (debug) {
            System.out.println("---------- P#" + ++i + " ----------");
            System.out.println(p.getText());
          }

        } else if (b.getElementType() == BodyElementType.TABLE) {
          ContainerTag table = table().withClasses("table", "header_table");

          XWPFTable t = (XWPFTable) b;
          if (debug)
            System.out.println("---------- T#" + ++i + " ----------");

          boolean head = true;
          for (XWPFTableRow r : t.getRows()) {
            ContainerTag tr = tr();
            StringBuilder s = new StringBuilder();
            s.append("[" + r.getTableCells().size() + "], ");

            for (XWPFTableCell c : r.getTableCells()) {

              String cText = c.getText().trim();
              if (cText.startsWith(Constants.CONST_DOCUMENT_ID) || cText.startsWith(Constants.CONST_DOCUMENT_VERSION)) {
                // DocumentID, DocumentVer: Get value in paragraphs
                for (XWPFParagraph p : c.getParagraphs()) {
                  String className = null;
                  String pText = p.getText().trim();
                  if (pText.startsWith(Constants.CONST_DOCUMENT_ID)) {
                    // Document ID
                    className = CLASS_NAME_DOC_ID;
                  } else if (pText.startsWith(Constants.CONST_DOCUMENT_VERSION)) {
                    // Document Version
                    // Remove non-numeric and non-decimal point values.
                    className = CLASS_NAME_DOC_VER;
                    pText = pText.replaceAll("[^\\d.]", "").trim();
                  }
                  addToTrTag(className, pText, tr, head);
                  s.append(pText.replaceAll("\\n", " ")).append(", ");
                }

              } else {
                // DocumentType: Get value in cells
                String docType = null;
                if (cText.matches("User Requirements Specification.*")) {
                  docType = "URS";
                } else if (cText.matches("Functional Requirements Specification.*")) {
                  docType = "FRS";
                } else if (cText.matches("Design and Configuration Specifications.*")) {
                  docType = "DCS";
                } else if (cText.matches("Test Script.*")) {
                  docType = "Test Script";
                } else {
                  // other doc type
                  docType = "Other (" + cText + ")";
                }

                addToTrTag(CLASS_NAME_DOC_TYPE, docType, tr, head);
                s.append(docType.replaceAll("\\n", " ")).append(", ");
              }
            }

            table.with(tr);
            if (debug)
              System.out.println(s);
            if (head) {
              head = false;
            }
          }
          body.with(table);
          setHeader = true;
        }
      }
      // Break when the process is done once. Because there are multiple headers.
      if (setHeader) {
        break;
      }
    }
  }

  private void addToTrTag(String className, String text, ContainerTag tr, Boolean head) {
    if (className != null) {
      tr.with(head ? th(text).withClass(className) : td(text).withClass(className));
    } else {
      tr.with(head ? th(text) : td(text));
    }
  }

  /**
   * Convert the document body to HTML.
   * 
   * @param docx
   * @param body
   */
  private void toHtmlFromBody(XWPFDocument docx, ContainerTag body) {
    // ------------------------
    // body
    // ------------------------

    // get number contents map
    Map<String, String> numsByTitle = this.getNumberContentsMap(docx.getParagraphs());

    String h1Class = null;
    String h2Class = null;
    String h3Class = null;
    String h4Class = null;
    String h5Class = null;
    ContainerTag div = null;
    Boolean testCaseDetailArea = false;
    List<String> paraList = new LinkedList<>();
    int i = 0;
    for (IBodyElement b : docx.getBodyElements()) {

      if (b.getElementType() == BodyElementType.PARAGRAPH) {
        XWPFParagraph para = (XWPFParagraph) b;
        if (para.getText() == null || para.getText().trim().length() == 0) {
          continue;
        }
        if (debug) {
          System.out.println("---------- P#" + ++i + " ----------");
          System.out.println(para.getText());
          System.out.println("NumFmt      : " + para.getNumFmt());
          System.out.println("NumLevelText: " + para.getNumLevelText());
          System.out.println("NumIlvl     : " + para.getNumIlvl());
          System.out.println("Style       : " + para.getStyle());
        }

        String text = para.getText().trim();
        String style = para.getStyle();

        // TestScript
        // Get the TestCaseID,TestCaseTitle before the first line of "TestCaseDetails".
        if (text.startsWith(Constants.PARA_TITLE_TEST_CASE_DETAILS)) {
          testCaseDetailArea = true;
          div = div().withClass(CLASS_NAME_TEST_CASE_DIV);
          String prevParaStr = paraList.get(paraList.size() - 1);
          String[] list = prevParaStr.trim().split("/", 0);
          if (list != null && list.length == 2) {
            div.with(h2(list[0]).withClass(CLASS_NAME_TEST_CASE_ID));
            div.with(h2(list[1]).withClass(CLASS_NAME_TEST_CASE_TITLE));
          } else {
            // Does not fit the format. Set value to TestCaseId.
            div.with(h2(list[0]).withClass(CLASS_NAME_TEST_CASE_ID));
          }
        }
        if (text.startsWith(Constants.PARA_TITLE_ENVIRONMENT) && div != null) {
          testCaseDetailArea = false;
          body.with(div);
          div = null;
        }

        // generate span tag with class
        // find paragraph linked to number contents.
        ContainerTag span = null;
        if (numsByTitle.containsKey(text)) {
          String num = numsByTitle.get(text);
          span = span(num).withClass(CLASS_NAME_ITEM_NO);
        }

        // generate h1,h2,h3,h4,h5,p tag
        ContainerTag tag = null;
        List<String> classes = new LinkedList<>();
        if ("Heading1".equalsIgnoreCase(style) || "1".equalsIgnoreCase(style) || "Title".equalsIgnoreCase(style)) {
          tag = h1();
          h1Class = generateClassName(text);
          classes.add(h1Class);
          h2Class = null;
          h3Class = null;
          h4Class = null;
          h5Class = null;
          if (debug)
            System.out.println("h1," + text);
        } else if ("Heading2".equalsIgnoreCase(style) || "2".equalsIgnoreCase(style)) {
          tag = h2();
          h2Class = generateClassName(text);
          if (isNotNull(h1Class, h2Class)) {
            classes.addAll(Arrays.asList(h1Class, h2Class));
          }
          h3Class = null;
          h4Class = null;
          h5Class = null;
          if (debug)
            System.out.println("h2," + text);
        } else if ("Heading3".equalsIgnoreCase(style) || "3".equalsIgnoreCase(style)) {
          tag = h3();
          h3Class = generateClassName(text);
          if (isNotNull(h1Class, h2Class, h3Class)) {
            classes.addAll(Arrays.asList(h1Class, h2Class, h3Class));
          }
          h4Class = null;
          h5Class = null;
          if (debug)
            System.out.println("h3," + text);
        } else if ("Heading4".equalsIgnoreCase(style) || "4".equalsIgnoreCase(style)) {
          tag = h4();
          h4Class = generateClassName(text);
          if (isNotNull(h1Class, h2Class, h3Class, h4Class)) {
            classes.addAll(Arrays.asList(h1Class, h2Class, h3Class, h4Class));
          }
          h5Class = null;
        } else if ("Heading5".equalsIgnoreCase(style) || "5".equalsIgnoreCase(style)) {
          tag = h5();
          h5Class = generateClassName(text);
          if (isNotNull(h1Class, h2Class, h3Class, h4Class, h5Class)) {
            classes.addAll(Arrays.asList(h1Class, h2Class, h3Class, h4Class, h5Class));
          }
        } else {
          tag = p();
          if (isNotNull(h1Class, h2Class, h3Class, h4Class, h5Class)) {
            classes.addAll(Arrays.asList(h1Class, h2Class, h3Class, h4Class, h5Class));
          }
        }

        // Make the classes up to ":".
        String captionClass = null;
        if (text.matches(".*:.*")) {
          captionClass = generateClassNameFromCaption(text);
          classes.add(captionClass);
        }

        // add class
        classes.removeAll(Collections.singleton(null));
        if (!classes.isEmpty()) {
          String classNames = String.join(" ", classes);
          tag = tag.withClass(classNames);
        }

        // add a span tag as a child.
        if (span != null) {
          tag.with(span).withText(text);
        } else {
          tag.withText(text);
        }

        if (testCaseDetailArea) {
          div.with(tag);
        } else {
          body.with(tag);
        }

        // add paragraph list.
        paraList.add(text);

      } else if (b.getElementType() == BodyElementType.TABLE) {
        ContainerTag table = table().withClasses("table", h1Class, h2Class, h3Class, h4Class, h5Class);

        XWPFTable t = (XWPFTable) b;
        if (debug)
          System.out.println("---------- T#" + ++i + " ----------");

        boolean head = true;
        int headerCellSize = 0;
        int cellSize = 0;
        List<String> classes = new ArrayList<>();
        for (XWPFTableRow r : t.getRows()) {
          ContainerTag tr = tr();
          StringBuilder s = new StringBuilder();
          s.append("[" + r.getTableCells().size() + "], ");

          int cellIdx = 0;
          cellSize = r.getTableCells().size();
          for (XWPFTableCell c : r.getTableCells()) {

            String classNameCaption = null;
            String text = null;
            if (head) {
              // table header: get value from cell. add generated class.
              text = c.getText();
              classes.add(generateClassName(text));
              headerCellSize = r.getTableCells().size();
            } else {
              // not table header: get value from paragraph.
              // Because end of sentence connects to beginning of next line.
              List<String> pTextList = new LinkedList<>();
              for (XWPFParagraph p : c.getParagraphs()) {
                pTextList.add(p.getText());
              }
              text = String.join(" ", pTextList);
              // cellSize == 1: generate class name of Requirement Category
              if (cellSize == 1 && c.getParagraphs().size() > 0) {
                String captionText = c.getParagraphs().get(0).getText();
                classNameCaption = generateClassName(captionText);
              }
            }
            text = text.trim();

            // generate tr tag
            if (!classes.isEmpty() && classes.size() > cellIdx && headerCellSize == cellSize) {
              tr.with(head ? th(text).withClass(classes.get(cellIdx)) : td(text).withClass(classes.get(cellIdx)));
            } else if (cellSize == 1 && classNameCaption != null) {
              tr.with(head ? th(text).withClass(classNameCaption) : td(text).withClass(classNameCaption));
            } else {
              tr.with(head ? th(text) : td(text));
            }

            s.append(text.replaceAll("\\n", " ")).append(", ");
            cellIdx++;
          }
          table.with(tr);
          if (debug)
            System.out.println(s);
          if (head)
            head = false;
        }
        body.with(table);
      } else {
        if (debug) {
          System.out.println("---------- C#" + ++i + " ----------");
          System.out.println("ElementType : " + b.getElementType());
          System.out.println("Body        : " + b.getBody());
        }
      }
    }
  }

  /**
   * get number contents map from paragraphs.
   * 
   * @param paras
   * @return map
   */
  private Map<String, String> getNumberContentsMap(List<XWPFParagraph> paras) {
    // get number contents(ex. "1. Purpose")
    Map<String, String> numsByTitle = new LinkedHashMap<>();
    for (int i = 0; i < paras.size(); i++) {
      String paraStr = paras.get(i).getText();
      if (paraStr == null || paraStr.equals("")) {
        continue;
      }

      // put to Map in the case of number contents.
      if (paraStr.matches(REGEX_NUMBERING)) {
        String[] texts = paraStr.split("\t");
        numsByTitle.put(texts[1], texts[0]);
        continue;
      }
    }
    // return empty map in the case of can't get number contents.
    if (numsByTitle.isEmpty()) {
      return new LinkedHashMap<>();
    }
    return numsByTitle;
  }

  /**
   * generate class name from caption.</br>
   * 
   * @param text
   * @return class name
   */
  private String generateClassNameFromCaption(String text) {
    String className = text.toLowerCase().trim();

    // Remove from the first ":" to the end.
    className = className.replaceAll(":.*", "");
    className = generateClassName(className);

    return className;
  }

  /**
   * generate class name.</br>
   * 
   * @param text
   * @return class name
   */
  private String generateClassName(String text) {
    String className = text.toLowerCase().trim();

    // Get class name from MAP if it matches the headline.
    for (String key : Constants.REQUIREMENT_CATEGORY_CLASS_MAP.keySet()) {
      if (className.equalsIgnoreCase(key)) {
        className = Constants.REQUIREMENT_CATEGORY_CLASS_MAP.get(key);
        return className;
      }
    }

    // remove !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
    className = className.replaceAll("\\p{Punct}", "").trim();
    // replace blank ("")
    for (String str : Constants.REPLACE_TO_BLANK) {
      className = className.replace(str, "").trim();
    }
    // replace underbar ("_")
    for (String str : Constants.REPLACE_TO_UNDERBAR) {
      className = className.replace(str, "_").trim();
    }
    // Replace one underbar when multiple underbar in a row.
    className = className.replaceAll("_{2,}", "_");

    return className;
  }

  /**
   * Determines if any of the classNames are set to a value.</br>
   *
   * @param classNames
   * @return Boolean
   */
  private Boolean isNotNull(String... classNames) {
    for (String className : classNames) {
      if (className != null) {
        return true;
      }
    }
    return false;
  }

}
