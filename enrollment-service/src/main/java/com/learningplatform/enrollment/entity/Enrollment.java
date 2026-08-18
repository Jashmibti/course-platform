package com.learningplatform.enrollment.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments",
       uniqueConstraints = @UniqueConstraint(name = "uk_enrollment_user_course", columnNames = {"user_id","course_id"}))
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id", nullable=false) private Long userId;
    @Column(name="course_id", nullable=false) private Long courseId;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private EnrollmentStatus status;
    @Column(name="enrolled_at", nullable=false) private LocalDateTime enrolledAt;

    public Enrollment() {}
    public Enrollment(Long userId, Long courseId, EnrollmentStatus status, LocalDateTime enrolledAt) {
        this.userId=userId; this.courseId=courseId; this.status=status; this.enrolledAt=enrolledAt;
    }
    public Long getId(){return id;}
    public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;}
    public Long getCourseId(){return courseId;} public void setCourseId(Long v){courseId=v;}
    public EnrollmentStatus getStatus(){return status;} public void setStatus(EnrollmentStatus v){status=v;}
    public LocalDateTime getEnrolledAt(){return enrolledAt;} public void setEnrolledAt(LocalDateTime v){enrolledAt=v;}
}
