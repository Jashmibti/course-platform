package com.learningplatform.course.service;

import com.learningplatform.course.dto.*;
import com.learningplatform.course.entity.*;
import com.learningplatform.course.event.CourseEventPublisher;
import com.learningplatform.course.event.LessonEventPublisher;
import com.learningplatform.course.exception.ResourceNotFoundException;
import com.learningplatform.course.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final TagRepository tagRepository;
    private final CourseEventPublisher eventPublisher;
    private final LessonEventPublisher lessonEventPublisher;

    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse create(CourseCreateRequest request, Long userId) {

        Course course = new Course();

        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setCategory(request.category());
        course.setLevel(request.level());
        course.setCreatedBy(userId);
        course.setTags(resolveTags(request.tags()));

        Course saved = courseRepository.save(course);

        eventPublisher.publishCreated(
                saved.getId(),
                saved.getCreatedBy(),
                saved.getTitle()
        );

        return toResponse(saved);
    }

    @Cacheable(value = "courses", key = "#id")
    @Transactional(readOnly = true)
    public CourseResponse get(Long id) {

        Course course = findCourse(id);

        // Initialize lazy relationships
        course.getModules().size();
        course.getTags().size();

        course.getModules().forEach(
                module -> module.getLessons().size()
        );

        return toResponse(course);
    }

    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse update(
            Long id,
            CourseUpdateRequest request) {

        Course course = findCourse(id);

        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setCategory(request.category());
        course.setLevel(request.level());
        course.setTags(resolveTags(request.tags()));

        eventPublisher.publishUpdated(
                course.getId(),
                course.getCreatedBy(),
                course.getTitle()
        );

        return toResponse(course);
    }

    @CacheEvict(value = "courses", allEntries = true)
    public void delete(Long id) {

        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Course not found: " + id
            );
        }

        courseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageResponse<CourseResponse> search(
            String keyword,
            String category,
            CourseLevel level,
            String tag,
            int page,
            int size) {

        int safeSize = Math.min(
                Math.max(size, 1),
                100
        );

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                safeSize,
                Sort.by("createdAt").descending()
        );

        String cleanKeyword = blankToNull(keyword);
        String cleanCategory = blankToNull(category);
        String cleanTag = blankToNull(tag);

        Page<Course> result;

        /*
         * If no search filters are provided,
         * simply return all courses.
         *
         * This avoids passing null String parameters
         * into the JPQL query and prevents PostgreSQL
         * from producing:
         *
         * ERROR: function lower(bytea) does not exist
         */
        if (cleanKeyword == null
                && cleanCategory == null
                && level == null
                && cleanTag == null) {

            result = courseRepository.findAll(pageable);

        } else {

            result = courseRepository.search(
                    cleanKeyword,
                    cleanCategory,
                    level,
                    cleanTag,
                    pageable
            );
        }

        return new PageResponse<>(
                result.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),

                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );
    }

    @CacheEvict(value = "courses", allEntries = true)
    public ModuleResponse addModule(
            Long courseId,
            ModuleCreateRequest request) {

        Course course = findCourse(courseId);

        CourseModule module = new CourseModule();

        module.setCourse(course);
        module.setTitle(request.title());
        module.setSequence(request.sequence());

        return toModuleResponse(
                moduleRepository.save(module)
        );
    }

    @Transactional(readOnly = true)
    public List<ModuleResponse> getModules(Long courseId) {

        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException(
                    "Course not found: " + courseId
            );
        }

        return moduleRepository
                .findByCourseIdOrderBySequenceAsc(courseId)
                .stream()
                .map(this::toModuleResponse)
                .toList();
    }

    @CacheEvict(value = "courses", allEntries = true)
        public LessonResponse addLesson(
                Long moduleId,
                LessonCreateRequest request) {

        CourseModule module = moduleRepository
                .findById(moduleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Module not found: " + moduleId
                        )
                );

        Lesson lesson = new Lesson();

        lesson.setModule(module);
        lesson.setTitle(request.title());
        lesson.setVideoUrl(request.videoUrl());
        lesson.setDuration(request.duration());

        Lesson saved = lessonRepository.save(lesson);

        lessonEventPublisher.publishLessonCreated(
                saved.getId(),
                module.getCourse().getId(),
                saved.getTitle()
        );

        return toLessonResponse(saved);
        }
    @Transactional(readOnly = true)
    public LessonResponse getLesson(Long lessonId) {

        Lesson lesson = lessonRepository
                .findById(lessonId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lesson not found: " + lessonId
                        )
                );

        return toLessonResponse(lesson);
    }

    @Transactional(readOnly = true)
    public Course findCourse(Long id) {

        return courseRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found: " + id
                        )
                );
    }

    private List<Tag> resolveTags(List<String> names) {

        if (names == null) {
            return new ArrayList<>();
        }

        return names.stream()
                .filter(name ->
                        name != null && !name.isBlank()
                )
                .map(String::trim)
                .distinct()
                .map(name ->
                        tagRepository
                                .findByNameIgnoreCase(name)
                                .orElseGet(() ->
                                        tagRepository.save(
                                                new Tag(name)
                                        )
                                )
                )
                .toList();
    }

    private CourseResponse toResponse(Course course) {

        List<TagResponse> tags = course
                .getTags()
                .stream()
                .map(tag ->
                        new TagResponse(
                                tag.getId(),
                                tag.getName()
                        )
                )
                .toList();

        List<ModuleResponse> modules = course
                .getModules()
                .stream()
                .map(this::toModuleResponse)
                .toList();

        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory(),
                course.getLevel(),
                course.getCreatedBy(),
                course.getCreatedAt(),
                course.getUpdatedAt(),
                tags,
                modules
        );
    }

    private ModuleResponse toModuleResponse(
            CourseModule module) {

        return new ModuleResponse(
                module.getId(),
                module.getTitle(),
                module.getSequence(),
                module.getLessons()
                        .stream()
                        .map(this::toLessonResponse)
                        .toList()
        );
    }

    private LessonResponse toLessonResponse(
            Lesson lesson) {

        return new LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getVideoUrl(),
                lesson.getDuration()
        );
    }

    private String blankToNull(String value) {

        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}