# Dockerfile for the project.
FROM openjdk:8-alpine
WORKDIR /tmp
COPY target/inventory-management-0.0.1-SNAPSHOT.jar /tmp/inventory-management-0.0.1-SNAPSHOT.jar
CMD ["/usr/bin/java", "-jar", "-Xms256m", "-Xmx512m", "-Dspring.profiles.active=${profile}", "/tmp/inventory-management-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080