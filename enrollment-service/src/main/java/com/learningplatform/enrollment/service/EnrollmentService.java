package com.learningplatform.enrollment.service;

import com.learningplatform.enrollment.dto.*;
import com.learningplatform.enrollment.entity.*;
import com.learningplatform.enrollment.exception.*;
import com.learningplatform.enrollment.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import com.learningplatform.enrollment.kafka.EnrollmentEvent;
import com.learningplatform.enrollment.kafka.EnrollmentEventProducer;

@Service
@Transactional
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final CourseCompletionRepository courseCompletionRepository;
    private final EnrollmentEventProducer eventProducer;

    public EnrollmentService(
            EnrollmentRepository e,
            LessonProgressRepository l,
            CourseCompletionRepository c,
            EnrollmentEventProducer eventProducer) {

        this.enrollmentRepository = e;
        this.lessonProgressRepository = l;
        this.courseCompletionRepository = c;
        this.eventProducer = eventProducer;
    }

    public EnrollStudentResponse enrollStudent(EnrollStudentRequest request){
        if(enrollmentRepository.existsByUserIdAndCourseId(request.getUserId(),request.getCourseId()))
            throw new EnrollmentAlreadyExistsException("User is already enrolled in this course");
        Enrollment saved=enrollmentRepository.save(
                new Enrollment(
                        request.getUserId(),
                        request.getCourseId(),
                        EnrollmentStatus.ACTIVE,
                        LocalDateTime.now()));

        eventProducer.publishCourseEnrolled(
                EnrollmentEvent.courseEnrolled(
                        saved.getUserId(),
                        saved.getCourseId()
                ));
        return new EnrollStudentResponse(saved.getId(),saved.getUserId(),saved.getCourseId(),saved.getStatus().name(),"Enrollment successful");
    }

    @Transactional(readOnly=true)
    public List<Enrollment> getUserEnrollments(Long userId){return enrollmentRepository.findByUserId(userId);}

    public LessonProgress markLessonComplete(MarkLessonCompleteRequest request){
        LessonProgress p=lessonProgressRepository.findByUserIdAndLessonId(request.getUserId(),request.getLessonId())
            .orElseGet(()->new LessonProgress(request.getUserId(),request.getLessonId(),false));
        p.setCompleted(true);

        LessonProgress saved =
                lessonProgressRepository.save(p);

        eventProducer.publishLessonCompleted(
                EnrollmentEvent.lessonCompleted(
                        saved.getUserId(),
                        saved.getLessonId()
                ));

        return saved;
    }

    @Transactional(readOnly=true)
    public ProgressResponse getProgress(Long userId,Long courseId){
        // The supplied project specification does not put course_id in lesson_progress.
        // Exact course percentage therefore requires Course Service lesson metadata.
        return new ProgressResponse(courseId,0);
    }

    public CourseCompletion completeCourse(CompleteCourseRequest request){
        Enrollment enrollment=enrollmentRepository.findByUserIdAndCourseId(request.getUserId(),request.getCourseId())
            .orElseThrow(()->new ResourceNotFoundException("User is not enrolled in this course"));
        if(courseCompletionRepository.existsByUserIdAndCourseId(request.getUserId(),request.getCourseId()))
            throw new IllegalStateException("Course is already completed by this user");
        CourseCompletion saved =
            courseCompletionRepository.save(
                    new CourseCompletion(
                            request.getUserId(),
                            request.getCourseId(),
                            LocalDateTime.now()));

    eventProducer.publishCourseCompleted(
            EnrollmentEvent.courseCompleted(
                    request.getUserId(),
                    request.getCourseId()
            ));

    enrollment.setStatus(
            EnrollmentStatus.COMPLETED);

    enrollmentRepository.save(enrollment);

    return saved;
    }
}
