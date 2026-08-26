FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/spotit-backend.jar app.jar
# Render sets $PORT at runtime; server.port in application.yml already reads it.
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
