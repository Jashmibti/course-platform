# Course Microservice

Course content management service for the Cloud-Native Online Learning Platform.

The project follows the supplied architecture: Java 21 + Spring Boot 3, PostgreSQL, Kafka, MinIO, Redis-ready caching, Actuator/Prometheus, and JWT resource-server security.

## 1. What this service owns

- Courses
- Modules
- Lessons
- Course tags
- Lesson video storage integration with MinIO
- Course-created and course-updated Kafka events

The Course Service has its own PostgreSQL database. Other microservices should not read its tables directly.

## 2. Project structure

```text
course-service/
├── src/main/java/com/learningplatform/course/
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── event/
│   ├── exception/
│   ├── repository/
│   └── service/
├── src/main/resources/
│   ├── application.yml
│   └── db/migration/V1__initial_schema.sql
├── src/test/
├── Dockerfile
├── docker-compose.yml
├── .env.example
└── pom.xml
```

## 3. APIs

Base URL: `http://localhost:8082/api/v1/courses`

### Course

- `POST /api/v1/courses`
- `GET /api/v1/courses/search`
- `GET /api/v1/courses/{courseId}`
- `PUT /api/v1/courses/{courseId}`
- `DELETE /api/v1/courses/{courseId}`

### Modules

- `POST /api/v1/courses/{courseId}/modules`
- `GET /api/v1/courses/{courseId}/modules`

### Lessons

- `POST /api/v1/courses/modules/{moduleId}/lessons`
- `GET /api/v1/courses/lessons/{lessonId}`

### Video

- `POST /api/v1/courses/lessons/{lessonId}/video`

The video endpoint accepts multipart form data with field name `file`, uploads to MinIO, and returns a temporary presigned URL.

## 4. Local Docker run

Prerequisite: Docker Desktop with Docker Compose.

Copy `.env.example` to `.env` if you want to customize credentials.

Start everything:

```bash
docker compose up --build
```

Check:

- Course service: `http://localhost:8082`
- Health: `http://localhost:8082/actuator/health`
- Prometheus metrics: `http://localhost:8082/actuator/prometheus`
- Swagger/OpenAPI: `http://localhost:8082/swagger-ui.html`
- MinIO API: `http://localhost:9000`
- MinIO console: `http://localhost:9001`
- PostgreSQL: `localhost:5432`
- Kafka: `localhost:9092`
- Redis: `localhost:6379`

Stop:

```bash
docker compose down
```

Stop and remove local data:

```bash
docker compose down -v
```

## 5. Important Docker networking rule

Inside Docker Compose, services must use Docker service names:

```text
postgres:5432
kafka:9092
minio:9000
redis:6379
```

Do not use `localhost` from inside the course-service container.

## 6. JWT/RBAC

The service is configured as an OAuth2 resource server using an HMAC JWT secret for local development.

The JWT must contain:

```json
{
  "sub": "101",
  "userId": 101,
  "roles": ["INSTRUCTOR"]
}
```

Supported course-management roles:

- `ADMIN`
- `INSTRUCTOR`

Students can read public course endpoints but cannot create/update/delete courses or manage content.

In the final platform, replace the local HMAC setup with Keycloak/OIDC as described by the project architecture.

## 7. Example create course request

```json
{
  "title": "Kubernetes Masterclass",
  "description": "Complete K8s training",
  "category": "devops",
  "level": "ADVANCED",
  "tags": ["kubernetes", "docker", "devops"]
}
```

## 8. Example module request

```json
{
  "title": "Introduction to Kubernetes",
  "sequence": 1
}
```

## 9. Example lesson request

```json
{
  "title": "Introduction",
  "videoUrl": "",
  "duration": 25
}
```

For the project sample, you can create 5-10 lessons and upload 5-10 videos to MinIO.

## 10. Kafka events

The service publishes:

- `course-created`
- `course-updated`

Event shape:

```json
{
  "eventId": "UUID",
  "eventType": "COURSE_CREATED",
  "courseId": 501,
  "createdBy": 101,
  "title": "Kubernetes Masterclass",
  "timestamp": "2026-01-01T14:22:00Z"
}
```

Recommendation and Reporting services should consume these events rather than calling this service directly for analytics.

## 11. Run only infrastructure, then run Java from IDE

If you have JDK/Maven locally:

```bash
docker compose up -d postgres kafka minio redis
mvn spring-boot:run
```

If Maven is not installed, use Docker:

```bash
docker compose up --build
```

For development, you can also use the Maven container:

```bash
docker run --rm -it \
  -v "$PWD":/workspace \
  -w /workspace \
  maven:3.9.11-eclipse-temurin-21 \
  mvn test
```

## 12. Deployment direction

For the final capstone deployment, this service can be packaged as:

```text
course-service Docker image
        |
        v
GHCR
        |
        v
Kubernetes Deployment
        |
        +--> ConfigMap
        +--> Secret
        +--> Service
        +--> HPA
        +--> Ingress
```

The application is intentionally kept stateless. PostgreSQL and MinIO hold persistent data.

## 13. Production hardening to add later

- Keycloak/OIDC instead of the local HMAC JWT
- Flyway as the authoritative database migration mechanism
- Redis-backed Spring Cache instead of local in-memory cache
- Kafka retry/DLT strategy
- Schema validation and event versioning
- Testcontainers integration tests
- OpenTelemetry tracing
- Network policies and Kubernetes Secrets
- Resource requests/limits and HPA
- CI/CD with GitHub Actions, GHCR, Trivy, CodeQL and SonarQube
