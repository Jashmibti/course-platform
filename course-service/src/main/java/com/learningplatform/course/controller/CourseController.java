package com.learningplatform.course.controller;

import com.learningplatform.course.dto.*;
import com.learningplatform.course.entity.CourseLevel;
import com.learningplatform.course.security.JwtService;
import com.learningplatform.course.service.CourseService;
import com.learningplatform.course.service.VideoStorageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CourseController {

    private final CourseService courseService;
    private final VideoStorageService videoStorageService;
    private final JwtService jwtService;

    // ============================================================
    // CREATE COURSE
    // ============================================================

    // Temporary local-development version:
    // Uses user ID 1 instead of requiring a JWT.
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse create(
            @Valid @RequestBody CourseCreateRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String role = jwtService.extractRole(token);

        if (!role.equals("ADMIN")
                && !role.equals("INSTRUCTOR")) {

            throw new RuntimeException(
                    "Only instructors and admins can create courses");
        }

        Long userId = jwtService.extractUserId(token);

        return courseService.create(request, userId);
    }
    // ============================================================
    // SEARCH COURSES
    // ============================================================

    @GetMapping("/search")
    public PageResponse<CourseResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) CourseLevel level,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return courseService.search(
                keyword,
                category,
                level,
                tag,
                page,
                size
        );
    }

    // ============================================================
    // GET COURSE
    // ============================================================

    @GetMapping("/{courseId}")
    public CourseResponse get(
            @PathVariable Long courseId) {

        return courseService.get(courseId);
    }

    // ============================================================
    // UPDATE COURSE
    // ============================================================

    @PutMapping("/{courseId}")
    public CourseResponse update(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseUpdateRequest request) {

        return courseService.update(courseId, request);
    }

    // ============================================================
    // DELETE COURSE
    // ============================================================

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long courseId) {

        courseService.delete(courseId);
    }

    // ============================================================
    // ADD MODULE TO COURSE
    // ============================================================

    @PostMapping("/{courseId}/modules")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleResponse addModule(
            @PathVariable Long courseId,
            @Valid @RequestBody ModuleCreateRequest request) {

        return courseService.addModule(courseId, request);
    }

    // ============================================================
    // GET MODULES FOR COURSE
    // ============================================================

    @GetMapping("/{courseId}/modules")
    public List<ModuleResponse> getModules(
            @PathVariable Long courseId) {

        return courseService.getModules(courseId);
    }

    // ============================================================
    // ADD LESSON TO MODULE
    // ============================================================

    @PostMapping("/modules/{moduleId}/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse addLesson(
            @PathVariable Long moduleId,
            @Valid @RequestBody LessonCreateRequest request) {

        return courseService.addLesson(moduleId, request);
    }

    // ============================================================
    // GET LESSON
    // ============================================================

    @GetMapping("/lessons/{lessonId}")
    public LessonResponse getLesson(
            @PathVariable Long lessonId) {

        return courseService.getLesson(lessonId);
    }

    // ============================================================
    // UPLOAD LESSON VIDEO
    // ============================================================

    @PostMapping(
            value = "/lessons/{lessonId}/video",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public VideoUploadResponse uploadVideo(
            @PathVariable Long lessonId,
            @RequestPart("file") MultipartFile file) {

        String objectName =
                videoStorageService.upload(lessonId, file);

        String url =
                videoStorageService.presignedUrl(objectName);

        return new VideoUploadResponse(
                lessonId,
                objectName,
                url
        );
    }
}