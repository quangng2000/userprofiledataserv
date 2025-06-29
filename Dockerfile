# Multi-stage Dockerfile for Spring Boot with MongoDB, Kafka, and OpenTelemetry

# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Set JAVA_HOME explicitly
ENV JAVA_HOME=/opt/java/openjdk

# Copy pom.xml files first for better layer caching
COPY pom.xml ./
COPY ./userprofiledataservService/pom.xml ./userprofiledataservService/

# Copy source code and needed files
COPY ./userprofiledataservService/src ./userprofiledataservService/src
COPY ./userprofiledataservService/otel.properties ./userprofiledataservService/

# Create dummy OpenAPI spec file
RUN mkdir -p ./userprofiledataservAPI && \
    echo '{"openapi":"3.0.0","info":{"title":"Dummy API","version":"1.0.0"},"paths":{}}' > \
    ./userprofiledataservAPI/bundled-openapi.yaml

# Build the application
RUN mvn clean package -pl userprofiledataservService -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Set JAVA_HOME explicitly
ENV JAVA_HOME=/opt/java/openjdk

# Install necessary packages
RUN apk add --no-cache curl

# Download OpenTelemetry Java agent
RUN curl -L -o opentelemetry-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

# Create directory for logs with appropriate permissions
RUN mkdir -p /app/logs && \
    chown -R 1000:1000 /app/logs && \
    chmod -R 777 /app/logs

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-javaagent:/app/opentelemetry-javaagent.jar \
    -Dotel.javaagent.configuration-file=/app/otel.properties \
    -Dspring.jmx.enabled=true \
    -Dio.micrometer.system-metrics.enabled=true \
    -Dmanagement.metrics.export.jmx.enabled=true \
    -Dmanagement.metrics.use-global-registry=true \
    -Dmanagement.metrics.export.prometheus.enabled=true \
    -Dmanagement.endpoint.metrics.enabled=true \
    -Dmanagement.endpoint.prometheus.enabled=true \
    -Dotel.resource.attributes=service.name=userprofiledataserv,service.namespace=riftco-core,deployment.environment=production \
    -Dotel.metrics.exporter=otlp \
    -Dotel.exporter.otlp.metrics.temporality_preference=delta \
    -Dotel.service.name=userprofiledataserv"

# Copy the JAR file from the build stage
COPY --from=build /app/userprofiledataservService/target/*.jar app.jar

# Copy OpenTelemetry properties file
COPY ./userprofiledataservService/otel.properties /app/otel.properties

# Expose the application port
EXPOSE 8080

# Health check using Spring Actuator endpoint
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set user to non-root for security best practices
USER 1000:1000

# Set entrypoint for running the application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
