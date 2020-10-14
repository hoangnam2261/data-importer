# SDLC Document Import Tool

## Requirements
* JDK 11
* MySQL server `8.0`

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