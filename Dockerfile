# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .
# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src
# Build the application
RUN mvn clean package -DskipTests

# Runtime stage - using distroless (smallest possible base image)
FROM gcr.io/distroless/java21-debian12:nonroot
WORKDIR /app

# Copy the built JAR
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check using curl (distroless has minimal tools)
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD ["java", "-cp", ".", "org.springframework.boot.loader.JarLauncher", "--health-check"] || exit 1

# Run with JVM optimizations for containers
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-XX:+UseStringDeduplication", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]

