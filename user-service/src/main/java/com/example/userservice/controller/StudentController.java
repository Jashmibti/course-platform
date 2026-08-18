package com.example.userservice.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @GetMapping("/courses")
    public String courses() {
        return "Student Courses";
    }
}