package com.learningplatform.enrollment.repository;

import com.learningplatform.enrollment.entity.CourseCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CourseCompletionRepository extends JpaRepository<CourseCompletion, Long> {
    Optional<CourseCompletion> findByUserIdAndCourseId(Long userId, Long courseId);
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
}
