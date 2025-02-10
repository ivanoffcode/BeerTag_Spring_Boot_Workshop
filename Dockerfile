# Stage 1: Build the application
FROM eclipse-temurin:17-jdk as build

WORKDIR /app

# Copy Gradle wrapper and source files
COPY gradlew gradlew.bat settings.gradle build.gradle /app/
COPY gradle /app/gradle
COPY src /app/src

# Grant execute permissions to Gradle wrapper
RUN chmod +x gradlew

# Build the application
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Create a lightweight runtime image
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]


