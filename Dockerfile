# Stage 1: Build the application using a Maven image that includes Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY . .
RUN mvn clean install -DskipTests

# Stage 2: Run the application using a lightweight Java 21 runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copy the built JAR file from the 'build' stage
COPY --from=build /app/target/SmartAttendanceSystem-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]