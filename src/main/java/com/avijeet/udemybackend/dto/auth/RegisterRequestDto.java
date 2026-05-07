package com.avijeet.udemybackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegisterRequestDto(
        @Email(message = "Invalid email format")
        @NotBlank
        String email,

        @Length(min = 8, message = "Password must be at least 8 characters long")
        @NotBlank
        String password,

        @NotBlank
        String role
) {}
