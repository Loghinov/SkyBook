package com.flightbooking.service.impl;

import com.flightbooking.dto.request.CreateBookingRequest;
import com.flightbooking.dto.response.BookingResponse;
import com.flightbooking.entity.*;
import com.flightbooking.exception.BookingException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.mapper.BookingMapper;
import com.flightbooking.repository.BookingRepository;
import com.flightbooking.repository.FlightRepository;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;

    @Override
    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));
        Flight flight = flightRepository.findById(request.flightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found: " + request.flightId()));

        if (flight.getAvailableSeats() < request.numberOfPassengers()) {
            throw new BookingException("Not enough available seats on flight " + flight.getFlightNumber());
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - request.numberOfPassengers());
        flightRepository.save(flight);

        BigDecimal totalPrice = flight.getPrice().multiply(BigDecimal.valueOf(request.numberOfPassengers()));
        Booking booking = Booking.builder()
                .user(user)
                .flight(flight)
                .status(BookingStatus.PENDING)
                .numberOfPassengers(request.numberOfPassengers())
                .totalPrice(totalPrice)
                .build();

        return BookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        return BookingMapper.toResponse(findBookingById(id));
    }

    @Override
    public List<BookingResponse> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long id) {
        Booking booking = findBookingById(id);
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException("Booking is already cancelled");
        }

        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + booking.getNumberOfPassengers());
        flightRepository.save(flight);

        booking.setStatus(BookingStatus.CANCELLED);
        return BookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    private Booking findBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }
}
