package com.learningplatform.reporting.dto;

public record CompletionRateResponse(
        long totalEnrollments,
        long totalCompletions,
        double completionRate
) {
}