# Enrollment Microservice

Java 21 / Spring Boot 3 Enrollment Service for the Cloud-Native Online Learning Platform.

## Scope from project specification

The Enrollment Service manages:
- enrollments
- lesson progress
- course completion

Documented APIs:
- POST /api/v1/enrollments
- GET /api/v1/enrollments/users/{userId}
- POST /api/v1/enrollments/progress
- GET /api/v1/enrollments/progress/{userId}/{courseId}
- POST /api/v1/enrollments/complete

## Important specification gap

The supplied specification defines `lesson_progress` as:
id, user_id, lesson_id, completed

but the progress API accepts `courseId`. Therefore this implementation leaves exact percentage calculation at 0 until Course Service lesson-to-course metadata is integrated. Do not treat this as the final production progress algorithm.

## Kafka

Topics:
- course-enrolled
- lesson-completed
- course-completed

## Docker

The Dockerfile builds Maven inside Docker, so Maven/Java do not need to be installed on the host.

Start everything:

    docker compose up --build

Stop:

    docker compose down

Reset database volume:

    docker compose down -v

Health:

    http://localhost:8083/actuator/health

## API examples

Enroll:
POST http://localhost:8083/api/v1/enrollments
Content-Type: application/json

{
  "userId": 101,
  "courseId": 501
}

List:
GET http://localhost:8083/api/v1/enrollments/users/101

Lesson complete:
POST http://localhost:8083/api/v1/enrollments/progress

{
  "userId": 101,
  "lessonId": 3001
}

Progress:
GET http://localhost:8083/api/v1/enrollments/progress/101/501

Complete:
POST http://localhost:8083/api/v1/enrollments/complete

{
  "userId": 101,
  "courseId": 501
}

## Implementation order

1. Project/package setup
2. Maven dependencies
3. Entities
4. Repositories
5. DTOs
6. Service/business layer
7. REST controller
8. Exception handling
9. PostgreSQL container
10. Kafka producer
11. Docker build/runtime
12. API testing
13. Course Service integration for exact progress
14. Security/gateway integration
15. Production Kubernetes/observability integration

## Architecture boundary

Enrollment Service owns enrollment data. User and Course are separate bounded contexts. Cross-service identifiers are stored as IDs rather than JPA relationships.
