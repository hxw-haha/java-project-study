FROM openjdk:8-jdk-alpine
LABEL maintainer="hanxw"
WORKDIR /app
# 请先在模块目录执行: mvn package -DskipTests
COPY target/*.jar app.jar
EXPOSE 8082 20882
ENTRYPOINT ["java", "-jar", "app.jar"]
