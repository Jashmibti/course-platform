package com.learningplatform.course.dto;

import java.util.List;

public record ModuleResponse(Long id, String title, Integer sequence, List<LessonResponse> lessons) {}
