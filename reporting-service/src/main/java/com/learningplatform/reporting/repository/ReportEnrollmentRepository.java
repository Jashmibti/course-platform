package com.learningplatform.reporting.repository;

import com.learningplatform.reporting.entity.ReportEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportEnrollmentRepository
        extends JpaRepository<ReportEnrollment, Long> {

    @Query(
            value = """
                    select course_id as courseId,
                           count(*) as enrollments
                    from report_enrollments
                    group by course_id
                    order by enrollments desc
                    limit 1
                    """,
            nativeQuery = true
    )
    PopularCourseProjection findMostPopularCourse();

    @Query("""
        select count(e)
        from ReportEnrollment e
        where e.userId = :userId
        """)
        long countByUserId(Long userId);
}