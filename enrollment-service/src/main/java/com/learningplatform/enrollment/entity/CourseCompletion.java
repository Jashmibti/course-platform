package com.learningplatform.enrollment.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="course_completion",
       uniqueConstraints = @UniqueConstraint(name="uk_course_completion_user_course", columnNames={"user_id","course_id"}))
public class CourseCompletion {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id", nullable=false) private Long userId;
    @Column(name="course_id", nullable=false) private Long courseId;
    @Column(name="completed_at", nullable=false) private LocalDateTime completedAt;

    public CourseCompletion(){}
    public CourseCompletion(Long userId, Long courseId, LocalDateTime completedAt){
        this.userId=userId; this.courseId=courseId; this.completedAt=completedAt;
    }
    public Long getId(){return id;}
    public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;}
    public Long getCourseId(){return courseId;} public void setCourseId(Long v){courseId=v;}
    public LocalDateTime getCompletedAt(){return completedAt;} public void setCompletedAt(LocalDateTime v){completedAt=v;}
}
