FROM amazoncorretto:17-alpine
ARG JAR_FILE=target/*.jar
ARG PROFILES
ARG ENV
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILEDS}", "-Dspring.env=${ENV}", "-jar", "app.jar"]