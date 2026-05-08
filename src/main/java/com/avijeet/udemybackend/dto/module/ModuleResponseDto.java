package com.avijeet.udemybackend.dto.module;

public record ModuleResponseDto(
        Long moduleId,
        String title,
        Integer orderIndex,
        Long courseId
) {}
