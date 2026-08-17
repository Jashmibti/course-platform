package com.learningplatform.reporting.service;

import com.learningplatform.reporting.repository.ReportCourseRepository;
import com.learningplatform.reporting.repository.ReportLessonRepository;
import com.learningplatform.reporting.repository.ReportEnrollmentRepository;
import com.learningplatform.reporting.repository.ReportCompletionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final ReportLessonRepository lessonRepository;
    private final ReportCourseRepository courseRepository;
    private final ReportEnrollmentRepository enrollmentRepository;
    private final ReportCompletionRepository completionRepository;

    public long totalLessons() {
        return lessonRepository.count();
    }

    public long totalCourses() {
        return courseRepository.count();
    }

    public long totalEnrollments() {
        return enrollmentRepository.count();
    }

    public long totalCompletions() {
        return completionRepository.count();
    }
}