# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /workspace

# Copy gradle wrapper and build files
COPY gradlew .
COPY gradlew.bat .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable (important for Linux container)
RUN chmod +x gradlew

# Copy the source code
COPY src ./src

# Build the application and create the executable jar
RUN ./gradlew bootJar

# Stage 2: Create the final, smaller image
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the executable jar from the builder stage
COPY --from=builder /workspace/build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
