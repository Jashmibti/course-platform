package com.learningplatform.enrollment.controller;

import com.learningplatform.enrollment.dto.*;
import com.learningplatform.enrollment.entity.*;
import com.learningplatform.enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.learningplatform.enrollment.security.JwtService;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final JwtService jwtService;

    public EnrollmentController(EnrollmentService enrollmentService , JwtService jwtService){this.enrollmentService=enrollmentService;
        this.jwtService = jwtService;
    }

    @PostMapping
    ResponseEntity<EnrollStudentResponse> enroll(
            @Valid @RequestBody EnrollStudentRequest r,
            @RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7);

        String role = jwtService.extractRole(token);

        if (!role.equals("STUDENT")) {
            throw new RuntimeException(
                    "Only students can enroll in courses");
        }

        Long userId = jwtService.extractUserId(token);

        r.setUserId(userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enrollStudent(r));
    }

    @GetMapping("/users/{userId}")
    ResponseEntity<List<Enrollment>> userEnrollments(@PathVariable Long userId){
        return ResponseEntity.ok(enrollmentService.getUserEnrollments(userId));
    }

    @PostMapping("/progress")
    ResponseEntity<LessonProgress> lessonComplete(
            @Valid @RequestBody MarkLessonCompleteRequest r,
            @RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7);

        Long userId = jwtService.extractUserId(token);

        r.setUserId(userId);

        return ResponseEntity.ok(
                enrollmentService.markLessonComplete(r));
    }

    @GetMapping("/progress/{userId}/{courseId}")
    ResponseEntity<ProgressResponse> progress(@PathVariable Long userId,@PathVariable Long courseId){
        return ResponseEntity.ok(enrollmentService.getProgress(userId,courseId));
    }

    @PostMapping("/complete")
    ResponseEntity<CourseCompletion> complete(
            @Valid @RequestBody CompleteCourseRequest r,
            @RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7);

        Long userId = jwtService.extractUserId(token);

        r.setUserId(userId);

        return ResponseEntity.ok(
                enrollmentService.completeCourse(r));
    }
}
