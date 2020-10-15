package com.astellas.poc.sdlc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.astellas.poc.sdlc.converter.HtmlConverter;

//@Component
public class ProcessRunner implements ApplicationRunner {

  public static final Logger logger = LoggerFactory.getLogger(ProcessRunner.class);

  @Autowired
  HtmlConverter htmlConverter;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    logger.info("process started.");

    String proc = getOptionValue(args, "proc");
    String root = getOptionValue(args, "dir");
    String project = getOptionValue(args, "project");

    File rootDir = new File(root);


    // This code is just for investigation. not for production.
    if ("conv".equals(proc)) {
      String dest = getOptionValue(args, "dest");
      File destDir = dest != null ? new File(dest) : rootDir;
      Collection<File> docs =  FileUtils.listFiles(rootDir, new String[] {"docx"}, true); //TODO: extensions should be configurable.

      for (File doc : docs) {
        String outFileName = doc.getName() + ".html";
        try(Writer writer = new BufferedWriter(new FileWriter(new File(destDir, outFileName), false))) {
          this.htmlConverter.convert(new FileInputStream(doc), writer, doc.getName());
        } catch (Exception e) {
          logger.error(String.format("Error occurred in processing %s", doc.getName()), e);
        }
      }
    }
  }


  protected String getOptionValue(ApplicationArguments args, String optionName) {
    if (args == null) {
      return null;
    }
    if (optionName == null) {
      throw new IllegalArgumentException("optionName is required.");
    }

    List<String> optVals = args.getOptionValues(optionName);
    if (optVals == null || optVals.isEmpty()) {
      return null;
    }

    return optVals.get(0);

  }

}
