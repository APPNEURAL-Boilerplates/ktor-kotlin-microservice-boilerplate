# Ktor Microservice Boilerplate

A production-minded Ktor/Kotlin REST microservice starter using Netty, Kotlin serialization, structured JSON responses, request IDs, tests, Docker, and GitHub Actions.

## Stack

- Kotlin/JVM
- Ktor 3.4.3
- Netty engine
- Kotlinx Serialization JSON
- Gradle Kotlin DSL
- JUnit 5 + Ktor test host
- Docker + Docker Compose

## Project structure

```txt
.
├─ src/
│  ├─ main/
│  │  ├─ kotlin/com/example/microservice/
│  │  │  ├─ Application.kt
│  │  │  ├─ clients/
│  │  │  ├─ common/
│  │  │  ├─ config/
│  │  │  ├─ events/
│  │  │  ├─ modules/
│  │  │  │  ├─ health/
│  │  │  │  ├─ items/
│  │  │  │  └─ root/
│  │  │  ├─ plugins/
│  │  │  └─ workers/
│  │  └─ resources/logback.xml
│  └─ test/kotlin/com/example/microservice/ApplicationTest.kt
├─ .github/workflows/ci.yml
├─ .env.example
├─ Dockerfile
├─ docker-compose.yml
├─ Makefile
├─ build.gradle.kts
└─ settings.gradle.kts
```

## Requirements

- JDK 21+
- Gradle 9.x, or generate the wrapper with `make wrapper`
- Docker, optional

## Run locally

```bash
cp .env.example .env
gradle run
```

Open:

```txt
http://localhost:8080
http://localhost:8080/api/v1/health
http://localhost:8080/api/v1/ready
```

## Generate Gradle wrapper

This ZIP does not include the Gradle wrapper JAR. Generate it once locally and commit the wrapper files if your team wants reproducible commands:

```bash
make wrapper
# then use ./gradlew run, ./gradlew test, ./gradlew build
```

## Main endpoints

```txt
GET  /                     Service metadata
GET  /api/v1/health        Health check
GET  /api/v1/ready         Readiness check
GET  /api/v1/items         List items
POST /api/v1/items         Create item
GET  /api/v1/items/{id}    Get item by ID
```

## Example request

```bash
curl -X POST http://localhost:8080/api/v1/items   -H "content-type: application/json"   -H "x-request-id: demo-request-1"   -d '{"name":"Keyboard","description":"Mechanical keyboard","price":99.99}'
```

## Test

```bash
gradle test
```

Full check:

```bash
make check
```

## Docker

```bash
cp .env.example .env
docker compose up --build
```

## Environment variables

| Variable | Default | Description |
|---|---:|---|
| `SERVICE_NAME` | `ktor-microservice` | Service name returned by metadata/health endpoints |
| `APP_ENV` | `local` | Runtime environment name |
| `APP_VERSION` | `0.1.0` | Version reported by the service |
| `HOST` | `0.0.0.0` | Bind host |
| `PORT` | `8080` | Bind port |
| `CORS_ALLOWED_ORIGINS` | `*` | Comma-separated allowed origins |

## Notes for production

- Replace `InMemoryItemRepository` with a real database repository.
- Keep API keys, passwords, and tokens out of source control.
- Restrict `CORS_ALLOWED_ORIGINS` in production.
- Add authentication middleware before exposing private endpoints.
- Replace `LoggingEventPublisher` with Kafka, RabbitMQ, NATS, SNS/SQS, or your preferred broker.
- Add OpenTelemetry or your platform's observability agent if needed.
