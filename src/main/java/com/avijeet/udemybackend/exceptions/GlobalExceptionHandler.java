package com.avijeet.udemybackend.exceptions;

import com.avijeet.udemybackend.exceptions.course.CourseAlreadyExistsException;
import com.avijeet.udemybackend.exceptions.course.CourseNotFoundException;
import com.avijeet.udemybackend.utils.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(new ApiResponse<>(true, errors, "Validation failed"), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CourseAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleCourseAlreadyExistsException(CourseAlreadyExistsException ex) {
        return new ResponseEntity<>(new ApiResponse<>(true, null, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleCourseNotFoundException(CourseNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse<>(true, null, ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
