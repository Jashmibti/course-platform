package com.example.userservice.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instructor")
public class InstructorController {

    @GetMapping("/courses")
    public String courses() {
        return "Instructor Courses";
    }
}