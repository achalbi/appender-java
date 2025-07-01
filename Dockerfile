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

# --- Distroless runtime stage (commented out for reference) ---
# FROM gcr.io/distroless/java21-debian12:nonroot
# WORKDIR /app
# COPY --from=builder /app/target/*.jar app.jar
# EXPOSE 8080
# HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
#   CMD ["java", "-cp", ".", "org.springframework.boot.loader.JarLauncher", "--health-check"] || exit 1
# ENTRYPOINT ["java", \
#   "-XX:+UseContainerSupport", \
#   "-XX:MaxRAMPercentage=75.0", \
#   "-XX:+UseG1GC", \
#   "-XX:+UseStringDeduplication", \
#   "-Djava.security.egd=file:/dev/./urandom", \
#   "-jar", "app.jar"]
# ------------------------------------------------------------

# Runtime stage - with tools and shell
FROM eclipse-temurin:21-jdk-alpine-3.21
WORKDIR /app

RUN apk add --no-cache \
    curl \
    iputils \
    netcat-openbsd \
    bind-tools \
    vim \
    less \
    procps

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-XX:+UseStringDeduplication", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]

