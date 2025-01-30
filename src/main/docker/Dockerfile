
FROM openjdk:21-slim

WORKDIR /opt/remitra

COPY ${project.build.directory}/${project.artifactId}-${project.version}.jar /opt/remitra/app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8095

CMD ["java", "-jar", "app.jar"]
