package com.learningplatform.course.event;

import java.time.OffsetDateTime;

public record LessonEvent(
        Long lessonId,
        Long courseId,
        String title,
        String eventType,
        OffsetDateTime timestamp
) {}