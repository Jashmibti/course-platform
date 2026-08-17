package com.learningplatform.reporting.consumer;

import com.learningplatform.reporting.entity.ReportLesson;
import com.learningplatform.reporting.kafka.LessonEvent;
import com.learningplatform.reporting.repository.ReportLessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonEventConsumer {

    private final ReportLessonRepository repository;

    @KafkaListener(
            topics = "lesson-created",
            groupId = "reporting-service",
            containerFactory =
                    "lessonKafkaListenerContainerFactory"
    )
    public void consume(LessonEvent event) {

        System.out.println(
                "Reporting received lesson: "
                        + event.lessonId());

        ReportLesson lesson =
                new ReportLesson();

        lesson.setLessonId(event.lessonId());
        lesson.setCourseId(event.courseId());
        lesson.setTitle(event.title());

        repository.save(lesson);
    }
}