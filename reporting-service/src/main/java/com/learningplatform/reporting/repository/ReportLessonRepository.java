package com.learningplatform.reporting.repository;

import com.learningplatform.reporting.entity.ReportLesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportLessonRepository
        extends JpaRepository<ReportLesson, Long> {
}