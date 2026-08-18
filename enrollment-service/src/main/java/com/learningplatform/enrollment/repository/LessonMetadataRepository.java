package com.learningplatform.enrollment.repository;

import com.learningplatform.enrollment.entity.LessonMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonMetadataRepository
        extends JpaRepository<LessonMetadata, Long> {
}