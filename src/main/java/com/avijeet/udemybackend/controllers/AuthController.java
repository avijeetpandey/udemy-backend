package com.avijeet.udemybackend.controllers;

import com.avijeet.udemybackend.dto.auth.AuthResponseDto;
import com.avijeet.udemybackend.dto.auth.LoginRequestDto;
import com.avijeet.udemybackend.dto.auth.RegisterRequestDto;
import com.avijeet.udemybackend.service.auth.AuthService;
import com.avijeet.udemybackend.utils.api.ApiResponse;
import com.avijeet.udemybackend.utils.api.ApiRoutes;
import com.avijeet.udemybackend.utils.api.BaseController;
import com.avijeet.udemybackend.utils.constants.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.AUTH_END_POINT)
@RequiredArgsConstructor
public class AuthController extends BaseController  {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@RequestBody  RegisterRequestDto registerRequestDto) {
        AuthResponseDto response = authService.register(registerRequestDto);
        return ok(ApiConstants.DONE_MESSAGE, response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        AuthResponseDto response = authService.login(loginRequestDto);
        return ok(ApiConstants.DONE_MESSAGE, response);
    }
}
