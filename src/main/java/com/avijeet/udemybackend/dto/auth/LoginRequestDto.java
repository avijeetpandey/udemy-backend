package com.avijeet.udemybackend.dto.auth;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record LoginRequestDto(
        @Email(message = "Invalid email format")
        String email,

        @Length(min = 8, message = "Password must be at least 8 characters long")
        String password
) { }
