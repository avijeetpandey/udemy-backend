package com.avijeet.udemybackend.dto.auth;

import lombok.Builder;

@Builder
public record AuthResponseDto(
        Long id,
        String email,
        String role,
        String accessToken
) { }