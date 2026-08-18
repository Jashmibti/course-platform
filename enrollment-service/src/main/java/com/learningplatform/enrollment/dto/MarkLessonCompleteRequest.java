package com.learningplatform.enrollment.dto;
import jakarta.validation.constraints.NotNull;
public class MarkLessonCompleteRequest {
    private Long userId; @NotNull private Long lessonId;
    public MarkLessonCompleteRequest(){}
    public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;}
    public Long getLessonId(){return lessonId;} public void setLessonId(Long v){lessonId=v;}
}
