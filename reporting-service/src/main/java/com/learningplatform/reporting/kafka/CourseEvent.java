package com.learningplatform.reporting.kafka;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CourseEvent(
        UUID eventId,
        String eventType,
        Long courseId,
        Long createdBy,
        String title,
        OffsetDateTime timestamp
) {
}