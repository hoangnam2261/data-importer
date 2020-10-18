package com.astellas.poc.sdlc;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import com.astellas.poc.sdlc.converter.HtmlParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import com.astellas.poc.sdlc.converter.HtmlConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@Slf4j
public class ProcessRunner implements ApplicationRunner {

  @Autowired
  HtmlConverter htmlConverter;

  @Autowired
  HtmlParser htmlParser;

  @Override
  public void run(ApplicationArguments args) {
    log.info("process started.");

    String proc = getOptionValue(args, "proc", false);
    String root = getOptionValue(args, "dir", true);
    String project = getOptionValue(args, "project", false);

    try {

      File rootDir = new File(root);

      // This code is just for investigation. not for production.
      if ("conv".equals(proc)) {
        if (project != null) {
          //process for 1 project
          File destDir = Path.of(root, project).toFile();
          processProjectFolder(destDir);
        } else {
          //process for all projects under --dir folder
          File[] values = rootDir.listFiles(File::isDirectory);
          if (values != null) {
            Stream.of(values)
                    .parallel()
                    .forEach(this::processProjectFolder);
          }
        }
      }
    } catch (Exception e) {
      log.error("There is an error when process rootFolder {}", root, e);
    }
    log.info("process ended.");
  }

  private void processProjectFolder(File destDir) {
    htmlParser.convert(destDir.getName(), FileUtils.listFiles(destDir, new String[]{"html"}, false));
  }

  protected String getOptionValue(ApplicationArguments args, String optionName, boolean required) {
    if (args == null) {
      return null;
    }
    if (optionName == null) {
      throw new IllegalArgumentException("optionName is required.");
    }

    List<String> optVals = args.getOptionValues(optionName);
    if (CollectionUtils.isEmpty(optVals)) {
      if (required) {
        throw new IllegalArgumentException(String.format("Parameter %s is required", optionName));
      }
      return null;
    }

    return optVals.get(0);

  }

}
