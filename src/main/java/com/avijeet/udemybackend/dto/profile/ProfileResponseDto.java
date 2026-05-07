package com.avijeet.udemybackend.dto.profile;

import lombok.Builder;

@Builder
public record ProfileResponseDto(
        Long profileId,
        String name,
        Integer age,
        String profession,
        String bio
) {}
