package com.learningplatform.reporting.service;

import com.learningplatform.reporting.repository.ReportCourseRepository;
import com.learningplatform.reporting.repository.ReportLessonRepository;
import com.learningplatform.reporting.repository.ReportEnrollmentRepository;
import com.learningplatform.reporting.repository.PopularCourseProjection;
import com.learningplatform.reporting.repository.ReportCompletionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.learningplatform.reporting.dto.PopularCourseResponse;
import com.learningplatform.reporting.dto.UserActivityResponse;
import com.learningplatform.reporting.dto.CompletionRateResponse;


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
    public PopularCourseResponse mostPopularCourse() {

        PopularCourseProjection result =
                enrollmentRepository.findMostPopularCourse();

        if (result == null) {
            return null;
        }

        return new PopularCourseResponse(
                result.getCourseId(),
                result.getEnrollments()
        );
    }
    public CompletionRateResponse completionRate() {

        long enrollments = enrollmentRepository.count();
        long completions = completionRepository.count();

        double rate = enrollments == 0
                ? 0.0
                : ((double) completions / enrollments) * 100;

        return new CompletionRateResponse(
                enrollments,
                completions,
                rate
        );
    }

    public UserActivityResponse userActivity(Long userId) {

        long enrollments =
                enrollmentRepository.countByUserId(userId);

        return new UserActivityResponse(
                userId,
                enrollments
        );
    }
}