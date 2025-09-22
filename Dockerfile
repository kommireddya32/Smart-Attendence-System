# Stage 1: Build the application using Maven
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY . .
RUN mvn clean install -DskipTests

# Stage 2: Run the application using a lightweight Java image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy the built JAR file from the 'build' stage
COPY --from=build /app/target/SmartAttendanceSystem-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]