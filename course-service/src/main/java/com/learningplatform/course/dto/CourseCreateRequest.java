package com.learningplatform.course.dto;

import com.learningplatform.course.entity.CourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CourseCreateRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank String description,
        @Size(max = 100) String category,
        @NotNull CourseLevel level,
        List<@Size(max = 100) String> tags
) {}
