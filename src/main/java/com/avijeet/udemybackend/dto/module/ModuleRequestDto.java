package com.avijeet.udemybackend.dto.module;

public record ModuleRequestDto(
        String title,
        Integer orderIndex,
        Long courseId
) {}
