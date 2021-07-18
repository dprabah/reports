## How to run the code
- It is a spring boot application, which has a main method in class `ReportApplication.java`
- Executing `ReportApplication.java` via IDE starts the tomcat and server.
- the landing page is [http://localhost:8080/][localhost]  
- Build Tool
    1. maven
- Open the project in IDE and issue command `mvn clean install`
- This installs required dependencies and runs the test.

## Properties files
- in `application.properties` file set `file.upload_dir` property, which defines where the csv file should save in server.

## How to run tests
- the maven command `mvn test` will execute tests.

[localhost]: http://localhost:8080/