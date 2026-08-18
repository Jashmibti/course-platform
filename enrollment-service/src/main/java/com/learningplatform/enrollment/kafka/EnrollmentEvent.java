package com.learningplatform.enrollment.kafka;

import java.time.LocalDateTime;
import java.util.UUID;

public record EnrollmentEvent(
    String eventId,
    String eventType,
    Long userId,
    Long courseId,
    Long lessonId,
    LocalDateTime timestamp
) {
    public static EnrollmentEvent courseEnrolled(Long userId, Long courseId) {
        return new EnrollmentEvent(UUID.randomUUID().toString(),"COURSE_ENROLLED",userId,courseId,null,LocalDateTime.now());
    }
    public static EnrollmentEvent lessonCompleted(Long userId, Long lessonId) {
        return new EnrollmentEvent(UUID.randomUUID().toString(),"LESSON_COMPLETED",userId,null,lessonId,LocalDateTime.now());
    }
    public static EnrollmentEvent courseCompleted(Long userId, Long courseId) {
        return new EnrollmentEvent(UUID.randomUUID().toString(),"COURSE_COMPLETED",userId,courseId,null,LocalDateTime.now());
    }
}
