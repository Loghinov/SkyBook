package com.flightbooking.service.impl;

import com.flightbooking.dto.request.CreateUserRequest;
import com.flightbooking.dto.response.UserResponse;
import com.flightbooking.entity.Role;
import com.flightbooking.entity.User;
import com.flightbooking.exception.BookingException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.mapper.UserMapper;
import com.flightbooking.repository.BookingRepository;
import com.flightbooking.repository.PaymentRepository;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BookingException("Email already registered: " + request.email());
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new BookingException("Username already taken: " + request.username());
        }
        User user = User.builder()
                .username(request.username())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .phoneNumber(request.phoneNumber())
                .build();
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUserById(Long id) {
        return UserMapper.toResponse(findUserById(id));
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return UserMapper.toResponse(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email))
        );
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = findUserById(id);
        if (user.getRole() == Role.ADMIN) {
            throw new BookingException("Cannot delete an admin account");
        }
        // Delete payments → bookings → user (manual cascade)
        bookingRepository.findByUserId(id).forEach(booking -> {
            paymentRepository.findByBookingId(booking.getId())
                    .ifPresent(paymentRepository::delete);
            bookingRepository.delete(booking);
        });
        userRepository.deleteById(id);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
