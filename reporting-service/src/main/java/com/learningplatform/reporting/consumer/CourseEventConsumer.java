package com.learningplatform.reporting.consumer;

import com.learningplatform.reporting.entity.ReportCourse;
import com.learningplatform.reporting.kafka.CourseEvent;
import com.learningplatform.reporting.repository.ReportCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseEventConsumer {

    private final ReportCourseRepository repository;

    @KafkaListener(
            topics = "course-created",
            groupId = "reporting-service",
            containerFactory =
                    "courseKafkaListenerContainerFactory"
    )
    public void consume(CourseEvent event) {

        System.out.println(
                "Reporting received course: "
                        + event.courseId());

        ReportCourse course =
                new ReportCourse();

        course.setCourseId(event.courseId());
        course.setCreatedBy(event.createdBy());
        course.setTitle(event.title());

        repository.save(course);
    }
}