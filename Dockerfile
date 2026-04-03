# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy only pom.xml first for dependency layer cache
COPY pom.xml .

# Download dependencies (cached unless pom.xml changes)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -f pom.xml dependency:go-offline -B --quiet 2>/dev/null || true

# Download Maven wrapper or use system Maven
# Alpine eclipse-temurin doesn't include Maven — install it
RUN apk add --no-cache maven

# Re-download with correct Maven
RUN mvn -f pom.xml dependency:go-offline -B --quiet 2>/dev/null || true

# Copy source
COPY spring-src ./spring-src

# Build (skip tests in Docker build — run in CI)
RUN mvn -f pom.xml package -DskipTests -B

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runner

# Non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy the fat jar
COPY --from=builder /build/target/blackout-api-*.jar app.jar

RUN chown appuser:appgroup app.jar
USER appuser

EXPOSE 3001

# JVM tuning for containers: virtual threads, small footprint
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
