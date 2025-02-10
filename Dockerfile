# Stage 1: Build the application
FROM eclipse-temurin:17-jdk AS build

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle wrapper files and the entire project source code
COPY . .

# Grant execute permissions to the Gradle wrapper
RUN chmod +x gradlew

# Build the application, skipping tests if necessary
RUN ./gradlew build -x test --no-daemon

# Stage 2: Create a lightweight runtime image
FROM openjdk:17-jre-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/beer-tag-0.1.0.jar app.jar

# Expose the port your application runs on (e.g., 8080)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

