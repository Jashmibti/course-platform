package com.learningplatform.reporting.repository;

import com.learningplatform.reporting.entity.ReportCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportCompletionRepository
        extends JpaRepository<ReportCompletion, Long> {
}