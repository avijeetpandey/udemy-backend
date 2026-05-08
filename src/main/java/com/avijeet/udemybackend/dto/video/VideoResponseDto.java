package com.avijeet.udemybackend.dto.video;

public record VideoResponseDto(
        String title,
        String videoUrl,
        Long moduleId,
        Long videoId
) {}
