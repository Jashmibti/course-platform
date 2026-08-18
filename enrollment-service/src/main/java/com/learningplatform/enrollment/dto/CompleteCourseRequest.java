package com.learningplatform.enrollment.dto;
import jakarta.validation.constraints.NotNull;
public class CompleteCourseRequest {
    private Long userId; @NotNull private Long courseId;
    public CompleteCourseRequest(){}
    public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;}
    public Long getCourseId(){return courseId;} public void setCourseId(Long v){courseId=v;}
}
