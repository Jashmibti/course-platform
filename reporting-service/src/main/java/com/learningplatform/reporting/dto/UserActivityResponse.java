package com.learningplatform.reporting.dto;

public record UserActivityResponse(
        Long userId,
        long enrollments
) {
}