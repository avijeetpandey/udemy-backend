package com.avijeet.udemybackend.mapper.auth;

import com.avijeet.udemybackend.dto.auth.AuthResponseDto;
import com.avijeet.udemybackend.dto.auth.RegisterRequestDto;
import com.avijeet.udemybackend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    User registerDtoToUser(RegisterRequestDto requestDto);

    @Mapping(target = "accessToken", source = "token")
    AuthResponseDto userToAuthResponseDto(User user, String token);
}
