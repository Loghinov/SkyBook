package com.flightbooking.controller;

import com.flightbooking.dto.request.LoginRequest;
import com.flightbooking.dto.request.RegisterRequest;
import com.flightbooking.dto.response.AuthResponse;
import com.flightbooking.entity.Role;
import com.flightbooking.entity.User;
import com.flightbooking.exception.BookingException;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.security.JwtUtil;
import com.flightbooking.security.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

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
        if (user.getRole() != Role.ADMIN) {
            throw new BookingException("This endpoint is for admin accounts only");
        }
        UserDetails ud = userDetailsService.loadUserByUsername(request.username());
        String token = jwtUtil.generateToken(ud, user.getRole().name(), user.getId());
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
        User admin = userRepository.save(User.builder()
                .username(request.username())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ADMIN)
                .phoneNumber(request.phoneNumber())
                .build());
        UserDetails ud = userDetailsService.loadUserByUsername(admin.getUsername());
        String token = jwtUtil.generateToken(ud, admin.getRole().name(), admin.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(
                token, admin.getUsername(), admin.getRole().name(),
                admin.getId(), admin.getFirstName() + " " + admin.getLastName()));
    }
}
