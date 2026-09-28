package com.flightbooking.service;

import com.flightbooking.dto.request.CreateBookingRequest;
import com.flightbooking.dto.response.BookingResponse;
import com.flightbooking.entity.*;
import com.flightbooking.exception.BookingException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.repository.BookingRepository;
import com.flightbooking.repository.FlightRepository;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock BookingRepository bookingRepository;
    @Mock FlightRepository flightRepository;
    @Mock UserRepository userRepository;

    @InjectMocks BookingServiceImpl service;

    private User user;
    private Flight flight;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L).username("alice").firstName("Alice").lastName("Smith")
                .email("alice@example.com").role(Role.USER)
                .createdAt(LocalDateTime.now()).build();

        flight = Flight.builder()
                .id(10L).flightNumber("AA101").origin("JFK").destination("LAX")
                .departureTime(LocalDateTime.now().plusDays(3))
                .arrivalTime(LocalDateTime.now().plusDays(3).plusHours(5))
                .price(new BigDecimal("299.99")).totalSeats(150).availableSeats(50).build();

        booking = Booking.builder()
                .id(100L).user(user).flight(flight)
                .status(BookingStatus.PENDING).numberOfPassengers(2)
                .totalPrice(new BigDecimal("599.98"))
                .bookedAt(LocalDateTime.now()).build();
    }

    @Test
    void createBooking_happyPath_returnsResponse() {
        CreateBookingRequest req = new CreateBookingRequest(1L, 10L, 2);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(flightRepository.findById(10L)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any())).thenReturn(flight);
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingResponse result = service.createBooking(req);

        assertThat(result.id()).isEqualTo(100L);
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.flightId()).isEqualTo(10L);
        assertThat(result.status()).isEqualTo(BookingStatus.PENDING);
        verify(flightRepository).save(flight);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_notEnoughSeats_throwsBookingException() {
        flight.setAvailableSeats(1);
        CreateBookingRequest req = new CreateBookingRequest(1L, 10L, 5);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(flightRepository.findById(10L)).thenReturn(Optional.of(flight));

        assertThatThrownBy(() -> service.createBooking(req))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Not enough available seats");
    }

    @Test
    void createBooking_userNotFound_throwsResourceNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createBooking(new CreateBookingRequest(99L, 10L, 1)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createBooking_flightNotFound_throwsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(flightRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createBooking(new CreateBookingRequest(1L, 99L, 1)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getBookingById_found_returnsResponse() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));

        BookingResponse result = service.getBookingById(100L);

        assertThat(result.id()).isEqualTo(100L);
    }

    @Test
    void getBookingById_notFound_throwsResourceNotFoundException() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getBookingById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getBookingsByUser_returnsList() {
        when(bookingRepository.findByUserId(1L)).thenReturn(List.of(booking));

        List<BookingResponse> result = service.getBookingsByUser(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).userId()).isEqualTo(1L);
    }

    @Test
    void cancelBooking_happyPath_setsCancelled() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        when(flightRepository.save(any())).thenReturn(flight);
        Booking cancelled = Booking.builder()
                .id(100L).user(user).flight(flight).status(BookingStatus.CANCELLED)
                .numberOfPassengers(2).totalPrice(booking.getTotalPrice())
                .bookedAt(booking.getBookedAt()).build();
        when(bookingRepository.save(any())).thenReturn(cancelled);

        BookingResponse result = service.cancelBooking(100L);

        assertThat(result.status()).isEqualTo(BookingStatus.CANCELLED);
        verify(flightRepository).save(flight);
    }

    @Test
    void cancelBooking_alreadyCancelled_throwsBookingException() {
        booking.setStatus(BookingStatus.CANCELLED);
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> service.cancelBooking(100L))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("already cancelled");
    }

    @Test
    void getAllBookings_returnsList() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<BookingResponse> result = service.getAllBookings();

        assertThat(result).hasSize(1);
    }
}