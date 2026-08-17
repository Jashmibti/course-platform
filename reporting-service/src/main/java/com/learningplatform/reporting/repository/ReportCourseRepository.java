package com.learningplatform.reporting.repository;

import com.learningplatform.reporting.entity.ReportCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportCourseRepository
        extends JpaRepository<ReportCourse, Long> {
}