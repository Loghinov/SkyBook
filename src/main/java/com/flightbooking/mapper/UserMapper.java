package com.flightbooking.mapper;

import com.flightbooking.dto.response.UserResponse;
import com.flightbooking.entity.User;

public class UserMapper {
    private UserMapper() {}

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}
