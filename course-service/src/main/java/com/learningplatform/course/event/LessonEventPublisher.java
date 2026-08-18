package com.learningplatform.course.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class LessonEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishLessonCreated(
            Long lessonId,
            Long courseId,
            String title) {

        LessonEvent event = new LessonEvent(
                lessonId,
                courseId,
                title,
                "LESSON_CREATED",
                OffsetDateTime.now()
        );

        kafkaTemplate.send(
                "lesson-created",
                String.valueOf(lessonId),
                event
        );
    }
}