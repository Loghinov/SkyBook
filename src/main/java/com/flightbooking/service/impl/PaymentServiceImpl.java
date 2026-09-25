package com.flightbooking.service.impl;

import com.flightbooking.dto.request.ProcessPaymentRequest;
import com.flightbooking.dto.response.PaymentResponse;
import com.flightbooking.entity.*;
import com.flightbooking.exception.BookingException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.mapper.PaymentMapper;
import com.flightbooking.repository.BookingRepository;
import com.flightbooking.repository.FlightRepository;
import com.flightbooking.repository.PaymentRepository;
import com.flightbooking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    @Override
    @Transactional
    public PaymentResponse processPayment(ProcessPaymentRequest request) {
        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + request.bookingId()));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException("Cannot pay for a cancelled booking");
        }
        if (paymentRepository.findByBookingId(booking.getId()).isPresent()) {
            throw new BookingException("Payment already processed for booking: " + booking.getId());
        }

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalPrice())
                .status(PaymentStatus.COMPLETED)
                .transactionId(UUID.randomUUID().toString())
                .processedAt(LocalDateTime.now())
                .build();

        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        return PaymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponse getPaymentByBookingId(Long bookingId) {
        return PaymentMapper.toResponse(
                paymentRepository.findByBookingId(bookingId)
                        .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking: " + bookingId))
        );
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new BookingException("Only completed payments can be refunded");
        }

        Booking booking = payment.getBooking();
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + booking.getNumberOfPassengers());
        flightRepository.save(flight);

        payment.setStatus(PaymentStatus.REFUNDED);
        return PaymentMapper.toResponse(paymentRepository.save(payment));
    }
}
