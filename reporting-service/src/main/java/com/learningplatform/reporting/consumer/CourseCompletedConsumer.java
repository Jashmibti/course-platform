package com.learningplatform.reporting.consumer;

import com.learningplatform.reporting.entity.ReportCompletion;
import com.learningplatform.reporting.kafka.EnrollmentEvent;
import com.learningplatform.reporting.repository.ReportCompletionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseCompletedConsumer {

    private final ReportCompletionRepository repository;

    @KafkaListener(
            topics = "course-completed",
            groupId = "reporting-service",
            containerFactory =
                    "enrollmentKafkaListenerContainerFactory"
    )
    public void consume(EnrollmentEvent event) {

        System.out.println(
                "Course completed: "
                        + event.courseId());

        ReportCompletion completion =
                new ReportCompletion();

        completion.setUserId(event.userId());
        completion.setCourseId(event.courseId());

        repository.save(completion);
    }
}