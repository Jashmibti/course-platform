package com.learningplatform.course.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CourseEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.course-created:course-created}")
    private String courseCreatedTopic;

    @Value("${app.kafka.topics.course-updated:course-updated}")
    private String courseUpdatedTopic;

    public void publishCreated(Long courseId, Long createdBy, String title) {
        publish(courseCreatedTopic, "COURSE_CREATED", courseId, createdBy, title);
    }

    public void publishUpdated(Long courseId, Long createdBy, String title) {
        publish(courseUpdatedTopic, "COURSE_UPDATED", courseId, createdBy, title);
    }

    private void publish(String topic, String eventType, Long courseId, Long createdBy, String title) {
        CourseEvent event = new CourseEvent(
                UUID.randomUUID(), eventType, courseId, createdBy, title, OffsetDateTime.now());
        kafkaTemplate.send(topic, String.valueOf(courseId), event);
    }
}
