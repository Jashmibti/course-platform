package com.learningplatform.course.repository;

import com.learningplatform.course.entity.Course;
import com.learningplatform.course.entity.CourseLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
        select distinct c
        from Course c
        left join c.tags t
        where (
            :keyword is null
            or lower(c.title) like lower(concat('%', :keyword, '%'))
            or lower(c.description) like lower(concat('%', :keyword, '%'))
        )
        and (
            :category is null
            or lower(c.category) = lower(:category)
        )
        and (
            :level is null
            or c.level = :level
        )
        and (
            :tag is null
            or lower(t.name) = lower(:tag)
        )
        """)
    Page<Course> search(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("level") CourseLevel level,
            @Param("tag") String tag,
            Pageable pageable
    );
}