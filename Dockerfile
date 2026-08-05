# syntax=docker/dockerfile:1.7

# ==========================================
# Phase 1: Build Stage (GraalVM native image)
# ==========================================
FROM ghcr.io/graalvm/native-image:latest AS builder
WORKDIR /app

# The native-image builder is intentionally minimal, so install the Unix utilities
# that Gradle's wrapper relies on during dependency resolution and build setup.
RUN microdnf install -y findutils && microdnf clean all

# Copy Gradle files first to maximize caching
COPY gradle /app/gradle
COPY gradlew /app/
COPY gradlew.bat /app/
COPY settings.gradle /app/
COPY build.gradle /app/

# Normalize line endings and pre-download dependencies
RUN chmod +x gradlew && \
    ./gradlew --no-daemon dependencies || true

# Copy source and build a GraalVM native binary (tests are skipped)
COPY src /app/src
RUN ./gradlew --no-daemon nativeCompile -x test

# ==========================================
# Phase 2: Production Runtime Stage
# ==========================================
FROM debian:bookworm-slim
WORKDIR /app

# Install only the minimum runtime libs needed by the binary in a small image
RUN apt-get update && apt-get install -y --no-install-recommends libc6 libgcc-s1 libz-dev && rm -rf /var/lib/apt/lists/*

# Create a non-root user
RUN useradd --system --create-home --uid 10001 appuser

# Create upload directory for local fallback storage
RUN mkdir -p /app/uploads && chown -R appuser:appuser /app/uploads
VOLUME /app/uploads

# Copy the compiled GraalVM native executable into the runtime image
COPY --from=builder /app/build/native/nativeCompile/afrorealityapi /app/app
RUN chown appuser:appuser /app/app

USER appuser

# Render will inject PORT automatically; the binary can consume it via Spring Boot env binding
EXPOSE 8080

CMD ["sh", "-c", "/app/app --server.port=${PORT:-8080}"]
