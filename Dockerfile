FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
# Tests run here so a failing test fails the image build and Render never deploys it.
RUN mvn -B clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/spotit-backend.jar app.jar
# Render sets $PORT at runtime; server.port in application.yml already reads it.
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
