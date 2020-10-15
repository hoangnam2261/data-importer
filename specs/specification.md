# Challenge Overview
## Overview

Welcome to Astellas’s “Data Import Tool Development” challenge!
Our client is a global pharmaceutical company headquartered in Japan, seeking to find solutions to enhance the software development documentation process. This challenge will be the first step in this research.

The goal of this challenge is to develop an ETL tool that extracts some data from HTML files and loads these data into tables in MySQL database.
HTML files are generated from MS-Word documents created for past system development projects in the client company. For some confidentiality reasons, the original Word files can not be made public. So, we will be providing documents as HTML with some dummy contents for this challenge.
 

## Document Types

The tool should support documents of the following types.

- User Requirement Specification (URS)

- Functional Requirement Specification (FRS)

- Design and Configuration Specification (DCS)

- Test Scripts

 
## Process flow

HTML files are collected in a folder of a related project. The tool should process multiple projects one by one.
It’s supposed that the root folder should be specified by the command-line parameter “--dir”.
It would be nice to have the option to specify a specific project to limit the target, as follows:
 --dir=/path/to/root --project="Project A"

For some project, the tool should process documents in the project folder in the following order:

1. URS

2. FRS

3. DCS

4. Test Script

### Notes:

- There could be multiple documents of the same type in the same project, and you will need to process all of them.
- Each folder under the specified root folder is considered a project folder.
 
<img width="375" height="282" alt="Service" src="https://github.com/hoangnam2261/data-importer/raw/main/specs/Challenge Specification - Data Import Tool.png">

 
### How to detect document type of a file

The type of document can be detected in an element with class=”document_type” in a document.
Don’t try to do this by file name. The possible types are:

- URS

- FRS

- DCS

- Test Script

 
### How to extract data from HTML

The tool should support CSS selector and XPath to extract data from HTML.

For maintainability, the logic for data extraction should be described as CSS selector or XPath in the configuration file. Hard-coded logics should be minimized.

For the format of the configuration file, YAML or JSON is better. You can integrate it with the application configuration YAML in Sprint Boot.

 
<img width="624" height="273" alt="Service" src="https://github.com/hoangnam2261/data-importer/raw/main/specs/Challenge Specification - Data Import Tool_1.png">

 
### How to import data in Database

The tool should create or update records in MySQL tables with data extracted from HTML files.

There are several entities prepared for storing data. See [Data Dictionary] for details about data items and mapping:

- Project - The parent entity of all other entities. A project is identified by its name, which is the same name as a project folder containing input files.

- URS / URS Detail - Entities related to URS documents. URS entity is prepared for some overview items in URS documents (Purpose, Scope, Assumptions, etc.) URS Detail entity is prepared for tables of requirement details described in the document.

- FRS / FRS Detail - Entities related to FRS documents. FRS entity is prepared for some overview items in FRS documents (Purpose, Scope, Assumptions, etc.) FRS Detail entity is prepared for tables of requirement details described in the document.

- URS-FRS Relation - Requirement details described in FRS documents have related URS requirements. This entity is prepared for these relations.

- DCS / DCS Items - Entities related to DCS documents. DCS entity is prepared for some overview items in DCS documents (Purpose, Scope, Assumptions, etc.) DCS Item entity is prepared for project-specific items described in the document.

- Test Script / Test Case / Script Instruction - Entities related to Test Script documents. Test Script entity is prepared for some overview items in Test Script documents (Test Category, Purpose, Pre-Requisites, etc.) Test Script will have one or more Test Cases. Test Case entity is prepared for test cases (Test Case ID, Test Case Title, Test Objective, etc.) Script Instruction entity is prepared for instruction steps of Test cases.


<img width="492" height="435" alt="Service" src="https://github.com/hoangnam2261/data-importer/raw/main/specs/スクリーンショット 2020-10-11 15.38.18.png">


ERD Summary

 
### How to update existing data

When importing some project which has been imported before, the tool should delete existing data before importing.


### User-friendly logging

Write information about the progress of the process into log. But no need any advanced tool for logging it's ok just to use slf4j logger.
 

### Technology Stack

- Java 11

- Spring Boot 2.3.3

- MySQL 8.0
