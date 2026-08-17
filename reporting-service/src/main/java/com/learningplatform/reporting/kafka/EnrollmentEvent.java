package com.learningplatform.reporting.kafka;

import java.time.LocalDateTime;

public record EnrollmentEvent(
        String eventId,
        String eventType,
        Long userId,
        Long courseId,
        Long lessonId,
        LocalDateTime timestamp
) {
}