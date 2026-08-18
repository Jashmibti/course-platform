package com.learningplatform.enrollment.dto;
public class ProgressResponse {
    private Long courseId; private double completionPercent;
    public ProgressResponse(){}
    public ProgressResponse(Long courseId,double completionPercent){this.courseId=courseId;this.completionPercent=completionPercent;}
    public Long getCourseId(){return courseId;} public void setCourseId(Long v){courseId=v;}
    public double getCompletionPercent(){return completionPercent;} public void setCompletionPercent(double v){completionPercent=v;}
}
