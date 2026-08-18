package com.learningplatform.course.repository;

import com.learningplatform.course.entity.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {
    List<CourseModule> findByCourseIdOrderBySequenceAsc(Long courseId);
}
