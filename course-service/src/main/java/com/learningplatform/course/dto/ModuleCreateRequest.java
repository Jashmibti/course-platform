package com.learningplatform.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ModuleCreateRequest(
        @NotBlank @Size(max = 200) String title,
        @NotNull Integer sequence
) {}
