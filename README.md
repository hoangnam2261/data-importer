# SDLC Document Import Tool

## Requirements
* JDK 11
* MySQL server `8.0`

## Link to ERD
`https://docs.google.com/spreadsheets/d/1KxQl1WfR2FG1R0Vtn7Dc-QOlUe5k6wQ2Z6_IeMp1eGU/edit#gid=2021277810`

## How to run mysql in docker
1. Get mysql image
`docker pull mysql/mysql-server:8.0`
2. Run docker
```docker run --name=mysql1 -p3306:3306 -e MYSQL_ROOT_PASSWORD=sdlc -e MYSQL_DATABASE=sdlc_poc -e MYSQL_USER=sdlc -e MYSQL_PASSWORD=sdlc -e MYSQL_ONETIME_PASSWORD=false -d mysql/mysql-server:8.0```
3. Connecting to MySQL Server from within the Container to change root password
    ```bash
    docker exec -it mysql1 mysql -uroot -p
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'sdlc';
    ```
4.Run sql file ```sdlc_schema_20201013.sql``` in container:
```https://dev.to/n350071/login-to-mysql-on-docker-and-run-a-sql-file-2bk7```
 
## Quick Start
1. Run MySQL server
1. Edit database settings on src/main/resources/application-default.yml
    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://{mysql server host}:{port}/{database}?useSSL=false
        username: {username}
        password: {password}
    ```
1. Build teh schema in the database w/ DDL.
1. Run the service
    ```bash
    ./gradlew bootRun
    ```

## Code formatting
We use (google code style)[https://google.github.io/styleguide/javaguide.html].
You might be able to find the code formatter setting file written for your editor here: https://github.com/google/styleguide

## Unit Test
Unit test is integrated in the build process.
You also can run the tests as below.
    ```bash
    ./gradlew test
    ```

## Build
1. Building with Gradle
    ```bash
    ./gradlew build
    ```

## Run
1. 
    ```bash
     ./gradlew bootRun --args="--proc=import --dir=C:\Users\LW81343\Desktop\base_code1\data-importer\testdata\root_folder --project=projectA"  
    ```
   There are 3 command-line parameters:
   - proc: this is name of process you want to run: import for "import HTML file to mysql"
   - dir (required) : this is the path to rootFolder.
           The rootFolder contains all project Folders like: projectA, projectB
   - project (optional): If do not provide this option, the application will process all folders in root folder.

## Update policy
   - In project folder, if it had any files(with .html extension) in that,
        application will delete all data related to that project includes: project, urs, frs, urs detail, frs detail, ...
        Otherwise, it will skip. 


     
TODO:

- update logic according to clarification in forums.
- category classes by configuration  
- code format
