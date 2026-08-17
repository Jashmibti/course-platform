package com.learningplatform.reporting.consumer;

import com.learningplatform.reporting.entity.ReportEnrollment;
import com.learningplatform.reporting.kafka.EnrollmentEvent;
import com.learningplatform.reporting.repository.ReportEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseEnrolledConsumer {

    private final ReportEnrollmentRepository repository;

    @KafkaListener(
            topics = "course-enrolled",
            groupId = "reporting-service",
            containerFactory =
                    "enrollmentKafkaListenerContainerFactory"
    )
    public void consume(EnrollmentEvent event) {

        System.out.println(
                "Enrollment received: "
                        + event.courseId());

        ReportEnrollment enrollment =
                new ReportEnrollment();

        enrollment.setUserId(event.userId());
        enrollment.setCourseId(event.courseId());

        repository.save(enrollment);
    }
}