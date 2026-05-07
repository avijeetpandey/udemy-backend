package com.avijeet.udemybackend.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ProfileRequestDto(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Profession cannot be blank")
        String profession,

        @NotBlank(message = "Bio cannot be blank")
        String bio,

        @Positive(message = "Age must be a positive number")
        Integer age
) {}
