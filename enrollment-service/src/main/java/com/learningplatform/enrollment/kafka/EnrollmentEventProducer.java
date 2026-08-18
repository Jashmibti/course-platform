package com.learningplatform.enrollment.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class EnrollmentEventProducer {
    public static final String COURSE_ENROLLED="course-enrolled";
    public static final String LESSON_COMPLETED="lesson-completed";
    public static final String COURSE_COMPLETED="course-completed";

    private final KafkaTemplate<String, EnrollmentEvent> kafkaTemplate;
    public EnrollmentEventProducer(KafkaTemplate<String, EnrollmentEvent> kafkaTemplate){this.kafkaTemplate=kafkaTemplate;}

    public void publishCourseEnrolled(EnrollmentEvent e){kafkaTemplate.send(COURSE_ENROLLED,String.valueOf(e.userId()),e);}
    public void publishLessonCompleted(EnrollmentEvent e){kafkaTemplate.send(LESSON_COMPLETED,String.valueOf(e.userId()),e);}
    public void publishCourseCompleted(EnrollmentEvent e){kafkaTemplate.send(COURSE_COMPLETED,String.valueOf(e.userId()),e);}
}
