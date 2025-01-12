FROM amazoncorretto:17-alpine

ARG JAR_FILE=target/*.jar
ARG PROFILES
ARG ENV

# 빌드된 JAR 파일을 컨테이너에 복사
COPY ${JAR_FILE} app.jar

# ENTRYPOINT에서 변수 사용
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dspring.env=${ENV}", "-jar", "app.jar"]
