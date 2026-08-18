package com.learningplatform.enrollment.kafka;

import java.time.OffsetDateTime;

public record LessonEvent(
        Long lessonId,
        Long courseId,
        String title,
        String eventType,
        OffsetDateTime timestamp
) {
}