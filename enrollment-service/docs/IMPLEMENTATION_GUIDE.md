# Enrollment Microservice - Scratch-to-Run Guide

This guide is intentionally aligned to the supplied project specification.

## 1. Create the project

Windows CMD:

mkdir enrollment-service
cd enrollment-service
mkdir src
mkdir src\main
mkdir src\main\java
mkdir src\main\resources
mkdir src\test
mkdir src\test\java
mkdir src\main\java\com
mkdir src\main\java\com\learningplatform
mkdir src\main\java\com\learningplatform\enrollment
mkdir src\main\java\com\learningplatform\enrollment\controller
mkdir src\main\java\com\learningplatform\enrollment\service
mkdir src\main\java\com\learningplatform\enrollment\repository
mkdir src\main\java\com\learningplatform\enrollment\entity
mkdir src\main\java\com\learningplatform\enrollment\dto
mkdir src\main\java\com\learningplatform\enrollment\exception
mkdir src\main\java\com\learningplatform\enrollment\kafka
mkdir src\main\java\com\learningplatform\enrollment\config

## 2. Important package rule

Open the `enrollment-service` root in VS Code. Do not open `src` or `src/main/java` as the project root.

Correct source root:

src/main/java

Example:
src/main/java/com/learningplatform/enrollment/EnrollmentServiceApplication.java

Package:
package com.learningplatform.enrollment;

Never use:
package main.java.com.learningplatform.enrollment;

If VS Code reports that package error, remove a bad `.vscode/settings.json` sourcePaths setting or set:
java.project.sourcePaths = ["src/main/java"]
Then run:
Java: Clean Java Language Server Workspace

## 3. Build without Maven installed on host

This project is designed to use Docker for Maven and runtime.

docker compose up --build

## 4. Verify

docker compose ps

Then:
http://localhost:8083/actuator/health

Expected health status:
UP

## 5. Test enrollment

POST /api/v1/enrollments

{
  "userId": 101,
  "courseId": 501
}

Expected HTTP status:
201 Created

## 6. Test duplicate enrollment

Send the same request again.

Expected:
409 Conflict

## 7. Test user enrollments

GET /api/v1/enrollments/users/101

## 8. Mark lesson complete

POST /api/v1/enrollments/progress

{
  "userId": 101,
  "lessonId": 3001
}

## 9. Complete course

POST /api/v1/enrollments/complete

{
  "userId": 101,
  "courseId": 501
}

## 10. Kafka

The service publishes:
course-enrolled
lesson-completed
course-completed

The topic event model includes eventId, eventType, userId, courseId, lessonId and timestamp.

## 11. Progress API limitation

The supplied project specification has a mismatch:
lesson_progress has user_id and lesson_id but no course_id,
while GET progress takes userId and courseId and expects a percentage.

Final calculation should be implemented only after the Course Service contract is agreed. A typical integration would obtain the course's lesson IDs from Course Service, then count completed lesson IDs for the user. This is an architectural implementation proposal, not a requirement stated by the supplied document.

## 12. Later integration

The supplied architecture also includes:
- API Gateway
- User Service
- Course Service
- Recommendation Service
- Reporting Service
- PostgreSQL
- Redis
- Kafka
- MinIO
- Prometheus
- Grafana
- Loki
- OpenTelemetry

Kubernetes deployment is a later phase.

## 13. Production hardening still to do

- JWT/security integration
- Gateway routing
- user/course validation contracts
- Kafka retry/DLT strategy
- idempotent event handling
- transactional outbox if required
- database migrations (Flyway/Liquibase)
- Testcontainers integration tests
- structured logging
- tracing
- metrics
- Kubernetes manifests/Helm
- CI/CD
- secret management
