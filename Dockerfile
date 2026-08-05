# ==========================================
# Phase 1: Build Stage (Multi-stage build)
# ==========================================
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy gradle files first for efficient caching of dependencies
COPY gradle /app/gradle
COPY gradlew /app/
COPY gradlew.bat /app/
COPY settings.gradle /app/
COPY build.gradle /app/

# Convert gradlew line endings to LF and download dependencies (offline preparation)
RUN apk add --no-cache dos2unix && \
    dos2unix gradlew && \
    chmod +x gradlew && \
    ./gradlew --no-daemon dependencies || true

# Copy source code and build the production-ready jar (skipping tests)
COPY src /app/src
RUN ./gradlew --no-daemon bootJar -x test

# ==========================================
# Phase 2: Production Runtime Stage
# ==========================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install system libraries needed by the scrimage WebP binary library
RUN apk add --no-cache libc6-compat libstdc++ gcompat

# Create a non-root system user for safety
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Create directory for local upload fallback storage (if R2 is disabled)
RUN mkdir -p /app/uploads && chown -R appuser:appgroup /app/uploads
VOLUME /app/uploads

# Copy compiled jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar
RUN chown appuser:appgroup app.jar

# Run the container under the safe non-root user account
USER appuser

# Expose port (Render automatically routes web services via PORT env var)
EXPOSE 8080

# Configure JVM for Render's 512MB free tier
ENTRYPOINT ["java", \
  "-Xmx330m", \
  "-Xms200m", \
  "-XX:+UseSerialGC", \
  "-XX:MaxMetaspaceSize=100m", \
  "-Xss512k", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
