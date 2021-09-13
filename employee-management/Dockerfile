FROM openjdk:11
ADD target/employee-management-0.0.1-SNAPSHOT.jar employee-management.jar
WORKDIR src/main/resources
ENTRYPOINT ["java", "-jar", "employee-management.jar"]
EXPOSE 8081
