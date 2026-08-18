package com.learningplatform.enrollment.kafka;

import com.learningplatform.enrollment.entity.LessonMetadata;
import com.learningplatform.enrollment.repository.LessonMetadataRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LessonEventConsumer {

    private final LessonMetadataRepository repository;

    public LessonEventConsumer(
            LessonMetadataRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(
            topics = "lesson-created",
            groupId = "enrollment-service",
            containerFactory = "lessonKafkaListenerContainerFactory"
    )
    public void consume(LessonEvent event) {

        LessonMetadata metadata =
                new LessonMetadata();

        metadata.setLessonId(event.lessonId());
        metadata.setCourseId(event.courseId());
        metadata.setTitle(event.title());

        repository.save(metadata);

        System.out.println(
                "Lesson received: " + event.lessonId()
        );
    }
}