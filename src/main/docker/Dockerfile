
FROM openjdk:21-slim

WORKDIR /opt/remitra

COPY ${JAR_FILE} /opt/remitra/app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8095

CMD ["java", "-jar", "/opt/remitra/app.jar"]
