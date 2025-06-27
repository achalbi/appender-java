# Build Java application
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
# Copy all source files
COPY . .
# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

