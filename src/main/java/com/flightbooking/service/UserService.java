package com.flightbooking.service;

import com.flightbooking.dto.request.CreateUserRequest;
import com.flightbooking.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getAllUsers();
    void deleteUser(Long id);
}
