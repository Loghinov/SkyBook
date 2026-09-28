package com.flightbooking.service;

import com.flightbooking.dto.request.CreateUserRequest;
import com.flightbooking.dto.response.UserResponse;
import com.flightbooking.entity.Booking;
import com.flightbooking.entity.BookingStatus;
import com.flightbooking.entity.Role;
import com.flightbooking.entity.User;
import com.flightbooking.exception.BookingException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.repository.BookingRepository;
import com.flightbooking.repository.PaymentRepository;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock BookingRepository bookingRepository;
    @Mock PaymentRepository paymentRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks UserServiceImpl service;

    private User alice;
    private User adminUser;

    @BeforeEach
    void setUp() {
        alice = User.builder()
                .id(1L).username("alice").firstName("Alice").lastName("Smith")
                .email("alice@example.com").role(Role.USER)
                .createdAt(LocalDateTime.now()).build();

        adminUser = User.builder()
                .id(99L).username("admin").firstName("Admin").lastName("User")
                .email("admin@system.local").role(Role.ADMIN)
                .createdAt(LocalDateTime.now()).build();
    }

    @Test
    void createUser_happyPath_encodesPasswordAndReturnsResponse() {
        CreateUserRequest req = new CreateUserRequest(
                "alice", "Alice", "Smith", "alice@example.com", "pass1word", "+1-212-555-0101");
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("pass1word")).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(alice);

        UserResponse result = service.createUser(req);

        assertThat(result.username()).isEqualTo("alice");
        assertThat(result.email()).isEqualTo("alice@example.com");
        verify(passwordEncoder).encode("pass1word");
    }

    @Test
    void createUser_duplicateEmail_throwsBookingException() {
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.createUser(
                new CreateUserRequest("alice", "Alice", "Smith", "alice@example.com", "pass1word", null)))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    void createUser_duplicateUsername_throwsBookingException() {
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThatThrownBy(() -> service.createUser(
                new CreateUserRequest("alice", "Alice", "Smith", "new@example.com", "pass1word", null)))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Username already taken");
    }

    @Test
    void getUserById_found_returnsResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(alice));

        UserResponse result = service.getUserById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.username()).isEqualTo("alice");
    }

    @Test
    void getUserById_notFound_throwsResourceNotFoundException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUserById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getUserByEmail_found_returnsResponse() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(alice));

        UserResponse result = service.getUserByEmail("alice@example.com");

        assertThat(result.email()).isEqualTo("alice@example.com");
    }

    @Test
    void getUserByEmail_notFound_throwsResourceNotFoundException() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUserByEmail("missing@example.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllUsers_returnsList() {
        when(userRepository.findAll()).thenReturn(List.of(alice, adminUser));

        List<UserResponse> result = service.getAllUsers();

        assertThat(result).hasSize(2);
    }

    @Test
    void deleteUser_happyPath_deletesUserAndCascade() {
        Booking booking = Booking.builder()
                .id(100L).user(alice).status(BookingStatus.CONFIRMED)
                .numberOfPassengers(1).totalPrice(BigDecimal.TEN)
                .bookedAt(LocalDateTime.now()).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
        when(bookingRepository.findByUserId(1L)).thenReturn(List.of(booking));
        when(paymentRepository.findByBookingId(100L)).thenReturn(Optional.empty());

        service.deleteUser(1L);

        verify(bookingRepository).delete(booking);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_adminAccount_throwsBookingException() {
        when(userRepository.findById(99L)).thenReturn(Optional.of(adminUser));

        assertThatThrownBy(() -> service.deleteUser(99L))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Cannot delete an admin account");
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void deleteUser_notFound_throwsResourceNotFoundException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteUser(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
