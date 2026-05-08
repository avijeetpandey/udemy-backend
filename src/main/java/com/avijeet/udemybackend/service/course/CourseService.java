package com.avijeet.udemybackend.service.course;

import com.avijeet.udemybackend.dto.course.CourseRequestDto;
import com.avijeet.udemybackend.dto.course.CourseResponseDto;
import com.avijeet.udemybackend.entities.Course;
import com.avijeet.udemybackend.exceptions.course.CourseAlreadyExistsException;
import com.avijeet.udemybackend.exceptions.course.CourseNotFoundException;
import com.avijeet.udemybackend.mapper.course.CourseMapper;
import com.avijeet.udemybackend.repository.course.CourseRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional
    public CourseResponseDto create(CourseRequestDto courseRequestDto) {
        log.info("Creating course: {}", courseRequestDto);

        if (courseRepository.existsByTitle(courseRequestDto.title())) {
            log.error("Course with title '{}' already exists", courseRequestDto.title());
            throw new CourseAlreadyExistsException("A course with this title already exists.");
        }

        try {
            Course course = courseMapper.courseDtoToCourse(courseRequestDto);
            Course createdCourse = courseRepository.save(course);
            log.info("Successfully created course with ID: {}", createdCourse.getId());
            return courseMapper.courseToCourseResponseDto(createdCourse);
        } catch (Exception e) {
            log.error("Failed to create course: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while creating the course", e);
        }
    }

    @Transactional(readOnly = true)
    public CourseResponseDto getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException("Course not found"));
        return courseMapper.courseToCourseResponseDto(course);
    }

    @Transactional
    public CourseResponseDto updateCourse(CourseRequestDto courseRequestDto, Long courseId) {
        log.info("Updating course with ID: {}", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course with ID " + courseId + " not found"));

        try {
            course.setTitle(courseRequestDto.title());
            course.setDescription(courseRequestDto.description());
            course.setPrice(courseRequestDto.price());
            course.setAuthor(courseRequestDto.author());

            if (courseRequestDto.tags() != null) {
                course.getTags().clear();
                course.getTags().addAll(List.of(courseRequestDto.tags()));
            } else {
                course.getTags().clear();
            }

            Course updatedCourse = courseRepository.save(course);
            log.info("Successfully updated course with ID: {}", updatedCourse.getId());
            return courseMapper.courseToCourseResponseDto(updatedCourse);
        } catch (Exception e) {
            log.error("Failed to update course with ID: {}. Error: {}", courseId, e.getMessage(), e);
            throw new RuntimeException("An error occurred while updating the course", e);
        }
    }
}
