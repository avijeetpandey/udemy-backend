package com.avijeet.udemybackend.mapper.profile;

import com.avijeet.udemybackend.dto.profile.ProfileRequestDto;
import com.avijeet.udemybackend.dto.profile.ProfileResponseDto;
import com.avijeet.udemybackend.entities.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper {
    Profile profileRequestDtoToProfile(ProfileRequestDto requestDto);

    @Mapping(source = "id", target = "profileId")
    ProfileResponseDto profileToProfileResponseDto(Profile profile);
}