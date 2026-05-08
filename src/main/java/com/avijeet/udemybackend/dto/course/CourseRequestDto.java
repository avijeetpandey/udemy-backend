package com.avijeet.udemybackend.dto.course;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CourseRequestDto(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        Double price,

        @NotBlank(message = "Author is required")
        String author,

        @NotEmpty(message = "Tags cannot be empty")
        String[] tags
) {}
