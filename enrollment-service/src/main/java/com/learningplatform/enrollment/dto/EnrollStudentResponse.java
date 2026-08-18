package com.learningplatform.enrollment.dto;
public class EnrollStudentResponse {
    private Long enrollmentId; private Long userId; private Long courseId; private String status; private String message;
    public EnrollStudentResponse(){}
    public EnrollStudentResponse(Long enrollmentId,Long userId,Long courseId,String status,String message){
        this.enrollmentId=enrollmentId;this.userId=userId;this.courseId=courseId;this.status=status;this.message=message;
    }
    public Long getEnrollmentId(){return enrollmentId;} public void setEnrollmentId(Long v){enrollmentId=v;}
    public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;}
    public Long getCourseId(){return courseId;} public void setCourseId(Long v){courseId=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
}
