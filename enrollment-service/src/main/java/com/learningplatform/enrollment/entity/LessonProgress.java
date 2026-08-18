package com.learningplatform.enrollment.entity;

import jakarta.persistence.*;

@Entity
@Table(name="lesson_progress",
       uniqueConstraints = @UniqueConstraint(name="uk_lesson_progress_user_lesson", columnNames={"user_id","lesson_id"}))
public class LessonProgress {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id", nullable=false) private Long userId;
    @Column(name="lesson_id", nullable=false) private Long lessonId;
    @Column(nullable=false) private boolean completed;

    public LessonProgress(){}
    public LessonProgress(Long userId, Long lessonId, boolean completed){
        this.userId=userId; this.lessonId=lessonId; this.completed=completed;
    }
    public Long getId(){return id;}
    public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;}
    public Long getLessonId(){return lessonId;} public void setLessonId(Long v){lessonId=v;}
    public boolean isCompleted(){return completed;} public void setCompleted(boolean v){completed=v;}
}
