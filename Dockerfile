FROM amazoncorrectto:11-alpine-jdk
ARG JAF_FILE=target/*.jar
ARG PROFILES
ARG ENV
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILEDS}", "-Dspring.env=${ENV}", "-jar", "app.jar"]