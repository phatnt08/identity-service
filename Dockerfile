# Stage 1: Build the application
FROM maven:3.9.9-amazoncorretto-23 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Stage 2: Create the runtime image
FROM amazoncorretto:23

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/identity-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot application runs on
EXPOSE 8080

# Set the default command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]