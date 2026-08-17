package com.learningplatform.reporting.controller;

import com.learningplatform.reporting.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService service;

    @GetMapping("/lessons/count")
    public Map<String, Long> countLessons() {

        return Map.of(
                "totalLessons",
                service.totalLessons()
        );
    }

    @GetMapping("/courses/count")
    public Map<String, Long> courseCount() {

        return Map.of(
                "totalCourses",
                service.totalCourses()
        );
    }
    @GetMapping("/enrollments/count")
    public Map<String, Long> enrollmentCount() {

        return Map.of(
                "totalEnrollments",
                service.totalEnrollments()
        );
    }

    @GetMapping("/completions/count")
    public Map<String, Long> completionCount() {

        return Map.of(
                "totalCompletions",
                service.totalCompletions()
        );
    }
}