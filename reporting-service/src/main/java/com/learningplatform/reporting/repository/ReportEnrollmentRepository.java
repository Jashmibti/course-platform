package com.learningplatform.reporting.repository;

import com.learningplatform.reporting.entity.ReportEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportEnrollmentRepository
        extends JpaRepository<ReportEnrollment, Long> {
}