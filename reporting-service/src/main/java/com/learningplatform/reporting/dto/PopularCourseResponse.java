package com.learningplatform.reporting.dto;

public record PopularCourseResponse(
        Long courseId,
        Long enrollments
) {
}