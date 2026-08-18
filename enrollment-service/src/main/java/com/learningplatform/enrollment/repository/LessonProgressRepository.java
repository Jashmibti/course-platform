package com.learningplatform.enrollment.repository;

import com.learningplatform.enrollment.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    Optional<LessonProgress> findByUserIdAndLessonId(Long userId, Long lessonId);
    List<LessonProgress> findByUserId(Long userId);
    boolean existsByUserIdAndLessonId(Long userId, Long lessonId);
}
