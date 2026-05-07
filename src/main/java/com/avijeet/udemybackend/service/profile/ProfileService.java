package com.avijeet.udemybackend.service.profile;

import java.util.Optional;
import com.avijeet.udemybackend.dto.profile.ProfileRequestDto;
import com.avijeet.udemybackend.dto.profile.ProfileResponseDto;
import com.avijeet.udemybackend.entities.Profile;
import com.avijeet.udemybackend.entities.User;
import com.avijeet.udemybackend.exceptions.profile.ProfileNotFoundException;
import com.avijeet.udemybackend.exceptions.user.UserNotFoundException;
import com.avijeet.udemybackend.mapper.profile.ProfileMapper;
import com.avijeet.udemybackend.repository.profile.ProfileRepository;
import com.avijeet.udemybackend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Transactional
    public ProfileResponseDto createProfile(ProfileRequestDto profileRequestDto, String email) {
        User user = Optional.ofNullable(userRepository.findUserByEmail(email))
                .orElseThrow(() -> {
                    log.error("User with email {} not found", email);
                    return new UserNotFoundException("User not found");
                });

        Profile profile = Profile.builder()
                .name(profileRequestDto.name())
                .age(profileRequestDto.age())
                .profession(profileRequestDto.profession())
                .bio(profileRequestDto.bio())
                .user(user)
                .build();

        profileRepository.save(profile);

        log.info("Created profile successfully with profileId: {}", profile.getId());

        return profileMapper.profileToProfileResponseDto(profile);
    }

    public ProfileResponseDto me(Long profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        return profileMapper.profileToProfileResponseDto(profile);
    }
}
