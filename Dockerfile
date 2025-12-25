# Stage 1: Build JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/journal_app.jar journal-app.jar 
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "journal-app.jar" ]