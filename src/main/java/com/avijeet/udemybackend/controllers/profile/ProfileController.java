package com.avijeet.udemybackend.controllers.profile;

import com.avijeet.udemybackend.dto.profile.ProfileRequestDto;
import com.avijeet.udemybackend.dto.profile.ProfileResponseDto;
import com.avijeet.udemybackend.service.profile.ProfileService;
import com.avijeet.udemybackend.utils.api.ApiResponse;
import com.avijeet.udemybackend.utils.api.ApiRoutes;
import com.avijeet.udemybackend.utils.api.BaseController;
import com.avijeet.udemybackend.utils.constants.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoutes.PROFILE_END_POINT)
@RequiredArgsConstructor
public class ProfileController extends BaseController {
    private final ProfileService profileService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<ProfileResponseDto>> create(
            @RequestBody ProfileRequestDto profileRequestDto,
            @AuthenticationPrincipal String email) {
        ProfileResponseDto response = profileService.createProfile(profileRequestDto, email);
        return ok(ApiConstants.DONE_MESSAGE, response);
    }

    @GetMapping("/me/{profileId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<ProfileResponseDto>> me(@PathVariable Long profileId) {
        ProfileResponseDto response = profileService.me(profileId);
        return ok(ApiConstants.DONE_MESSAGE, response);
    }
}
