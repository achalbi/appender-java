# Java API Service Starter

This is a minimal Java API service starter based on [Google Cloud Run Quickstart](https://cloud.google.com/run/docs/quickstarts/build-and-deploy/deploy-java-service).

## Getting Started

Server should run automatically when starting a workspace. To run manually, run:
```sh
mvn spring-boot:run
```

## Configuring Log Level

You can control the log level at runtime using environment variables or by editing the properties files.

### Using Environment Variables

Set the log level for your application or any package by passing an environment variable when starting the app or container:

```
SPRING_APPLICATION_JSON='{"logging.level.com.telemetrix":"DEBUG"}'
```
Or for Docker:
```
docker run -e SPRING_APPLICATION_JSON='{"logging.level.com.telemetrix":"DEBUG"}' appender-java:latest
```

You can also set specific log levels for Spring or other packages:
```
SPRING_APPLICATION_JSON='{"logging.level.org.springframework.web":"INFO"}'
```

### Using Properties Files

- For development: edit `src/main/resources/application.properties`
- For production: edit `src/main/resources/application-prod.properties`

Example:
```
logging.level.com.telemetrix=DEBUG
logging.level.org.springframework.web=INFO
```

### Supported Log Levels
- TRACE
- DEBUG
- INFO
- WARN
- ERROR
- FATAL
- OFF