
FROM openjdk:17-jdk-slim

WORKDIR /opt/remitra

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /opt/remitra/app.jar

EXPOSE 8095

CMD ["java", "-jar", "/opt/remitra/app.jar"]
