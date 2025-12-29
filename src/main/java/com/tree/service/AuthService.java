package com.tree.service;

import com.tree.dto.auth.AuthResponse;
import com.tree.dto.auth.LoginRequest;
import com.tree.dto.auth.RegisterRequest;
import com.tree.entity.User;
import com.tree.exception.BadRequestException;
import com.tree.repository.UserRepository;
import com.tree.security.JwtTokenProvider;
import com.tree.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = tokenProvider.generateToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        log.info("Login successful for email: {}", request.getEmail());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .user(AuthResponse.UserDto.builder()
                        .id(userPrincipal.getId().toString())
                        .email(userPrincipal.getEmail())
                        .fullName(userPrincipal.getFullName())
                        .role(userPrincipal.getRole())
                        .build())
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registration attempt for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(User.Role.ADMIN)
                .active(true)
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", request.getEmail());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(request.getEmail());
        loginRequest.setPassword(request.getPassword());

        return login(loginRequest);
    }
}
