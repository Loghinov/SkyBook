package com.flightbooking.controller;

import com.flightbooking.dto.request.LoginRequest;
import com.flightbooking.dto.request.RegisterRequest;
import com.flightbooking.dto.request.UpdateProfileRequest;
import com.flightbooking.dto.response.AuthResponse;
import com.flightbooking.dto.response.UserResponse;
import com.flightbooking.entity.Role;
import com.flightbooking.entity.User;
import com.flightbooking.exception.BookingException;
import com.flightbooking.mapper.UserMapper;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.security.JwtUtil;
import com.flightbooking.security.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        User user = userRepository.findByUsername(request.username()).orElseThrow();
        String token = jwtUtil.generateToken(
                userDetailsService.loadUserByUsername(request.username()),
                user.getRole().name(), user.getId());
        return ResponseEntity.ok(new AuthResponse(
                token, user.getUsername(), user.getRole().name(),
                user.getId(), user.getFirstName() + " " + user.getLastName()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.email()))
            throw new BookingException("Email already registered");
        if (userRepository.existsByUsername(request.username()))
            throw new BookingException("Username already taken");
        User user = userRepository.save(User.builder()
                .username(request.username())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .phoneNumber(request.phoneNumber())
                .build());
        UserDetails ud = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(ud, user.getRole().name(), user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(
                token, user.getUsername(), user.getRole().name(),
                user.getId(), user.getFirstName() + " " + user.getLastName()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        return ResponseEntity.ok(UserMapper.toResponse(userRepository.save(user)));
    }
}
