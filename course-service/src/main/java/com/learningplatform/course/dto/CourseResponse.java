package com.learningplatform.course.dto;

import com.learningplatform.course.entity.CourseLevel;

import java.time.OffsetDateTime;
import java.util.List;

public record CourseResponse(
        Long id,
        String title,
        String description,
        String category,
        CourseLevel level,
        Long createdBy,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<TagResponse> tags,
        List<ModuleResponse> modules
) {}
