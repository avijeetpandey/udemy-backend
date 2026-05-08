package com.avijeet.udemybackend.dto.course;

public record CourseResponseDto(
        Long courseId,
        String title,
        String description,
        Double price,
        String author,
        String[] tags
) {}
