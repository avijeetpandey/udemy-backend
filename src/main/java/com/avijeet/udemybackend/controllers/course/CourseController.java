package com.avijeet.udemybackend.controllers.course;

import com.avijeet.udemybackend.dto.course.CourseRequestDto;
import com.avijeet.udemybackend.dto.course.CourseResponseDto;
import com.avijeet.udemybackend.service.course.CourseService;
import com.avijeet.udemybackend.utils.api.ApiResponse;
import com.avijeet.udemybackend.utils.api.ApiRoutes;
import com.avijeet.udemybackend.utils.api.BaseController;
import com.avijeet.udemybackend.utils.constants.ApiConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoutes.COURSE_END_POINT)
@RequiredArgsConstructor
public class CourseController extends BaseController {
    private final CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CourseResponseDto>> create(@Valid @RequestBody CourseRequestDto courseRequestDto) {
        CourseResponseDto response = courseService.create(courseRequestDto);
        return ok(ApiConstants.DONE_MESSAGE, response);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseResponseDto>> getCourse(@PathVariable Long courseId) {
        CourseResponseDto response = courseService.getCourse(courseId);
        return ok(ApiConstants.DONE_MESSAGE, response);
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<ApiResponse<CourseResponseDto>> updateCourse(@Valid @RequestBody CourseRequestDto courseRequestDto, @PathVariable Long courseId) {
        CourseResponseDto response = courseService.updateCourse(courseRequestDto, courseId);
        return ok(ApiConstants.DONE_MESSAGE, response);
    }
}
