package com.avijeet.udemybackend.service.auth;

import com.avijeet.udemybackend.dto.auth.AuthResponseDto;
import com.avijeet.udemybackend.dto.auth.LoginRequestDto;
import com.avijeet.udemybackend.dto.auth.RegisterRequestDto;
import com.avijeet.udemybackend.entities.User;
import com.avijeet.udemybackend.enums.Role;
import com.avijeet.udemybackend.exceptions.user.UserAlreadyExistsException;
import com.avijeet.udemybackend.exceptions.user.UserNotFoundException;
import com.avijeet.udemybackend.mapper.auth.AuthMapper;
import com.avijeet.udemybackend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        log.info("Attempting to register new user with email: {}", request.email());

        if (userRepository.findUserByEmail(request.email()) != null) {
            log.warn("Registration failed: User with email {} already exists", request.email());
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
        }

        Role role;
        try {
            role = Role.valueOf(request.role().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("Registration failed: Invalid role provided - {}", request.role());
            throw new IllegalArgumentException("Invalid role provided");
        }
        
        User newUser = authMapper.registerDtoToUser(request);
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(role);

        // Save the user
        User savedUser = userRepository.save(newUser);
        log.info("Successfully registered user with id: {}", savedUser.getId());

        // Generate token using the saved user's email
        String accessToken = jwtService.generateToken(savedUser.getEmail());

        // Map entity and token to response DTO
        return authMapper.userToAuthResponseDto(savedUser, accessToken);
    }

    public AuthResponseDto login(LoginRequestDto request) {
        User user = userRepository.findUserByEmail(request.email());

        if ( user == null) {
            log.warn("Login failed: User with email {} does not exists", request.email());
            throw new UserNotFoundException("User with email " + request.email() + " does not exists");
        }

        String accessToken = jwtService.generateToken(request.email());

        return authMapper.userToAuthResponseDto(user, accessToken);
    }
}