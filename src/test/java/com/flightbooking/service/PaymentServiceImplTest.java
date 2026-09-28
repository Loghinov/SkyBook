package com.flightbooking.service;

import com.flightbooking.dto.request.ProcessPaymentRequest;
import com.flightbooking.dto.response.PaymentResponse;
import com.flightbooking.entity.*;
import com.flightbooking.exception.BookingException;
import com.flightbooking.exception.ResourceNotFoundException;
import com.flightbooking.repository.BookingRepository;
import com.flightbooking.repository.FlightRepository;
import com.flightbooking.repository.PaymentRepository;
import com.flightbooking.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock PaymentRepository paymentRepository;
    @Mock BookingRepository bookingRepository;
    @Mock FlightRepository flightRepository;

    @InjectMocks PaymentServiceImpl service;

    private User user;
    private Flight flight;
    private Booking pendingBooking;
    private Payment completedPayment;

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
                .price(new BigDecimal("299.99")).totalSeats(150).availableSeats(148).build();

        pendingBooking = Booking.builder()
                .id(100L).user(user).flight(flight).status(BookingStatus.PENDING)
                .numberOfPassengers(2).totalPrice(new BigDecimal("599.98"))
                .bookedAt(LocalDateTime.now()).build();

        completedPayment = Payment.builder()
                .id(200L).booking(pendingBooking).amount(new BigDecimal("599.98"))
                .status(PaymentStatus.COMPLETED).transactionId("TXN-ABC")
                .processedAt(LocalDateTime.now()).build();
    }

    @Test
    void processPayment_happyPath_returnsCompleted() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(pendingBooking));
        when(paymentRepository.findByBookingId(100L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any())).thenReturn(completedPayment);
        when(bookingRepository.save(any())).thenReturn(pendingBooking);

        PaymentResponse result = service.processPayment(new ProcessPaymentRequest(100L));

        assertThat(result.status()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(result.bookingId()).isEqualTo(100L);
        verify(bookingRepository).save(pendingBooking);
    }

    @Test
    void processPayment_cancelledBooking_throwsBookingException() {
        pendingBooking.setStatus(BookingStatus.CANCELLED);
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(pendingBooking));

        assertThatThrownBy(() -> service.processPayment(new ProcessPaymentRequest(100L)))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("cancelled booking");
    }

    @Test
    void processPayment_alreadyPaid_throwsBookingException() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(pendingBooking));
        when(paymentRepository.findByBookingId(100L)).thenReturn(Optional.of(completedPayment));

        assertThatThrownBy(() -> service.processPayment(new ProcessPaymentRequest(100L)))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Payment already processed");
    }

    @Test
    void processPayment_bookingNotFound_throwsResourceNotFoundException() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.processPayment(new ProcessPaymentRequest(999L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getPaymentByBookingId_found_returnsResponse() {
        when(paymentRepository.findByBookingId(100L)).thenReturn(Optional.of(completedPayment));

        PaymentResponse result = service.getPaymentByBookingId(100L);

        assertThat(result.id()).isEqualTo(200L);
        assertThat(result.status()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    void getPaymentByBookingId_notFound_throwsResourceNotFoundException() {
        when(paymentRepository.findByBookingId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getPaymentByBookingId(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void refundPayment_happyPath_setsRefunded() {
        Payment refundedPayment = Payment.builder()
                .id(200L).booking(pendingBooking).amount(new BigDecimal("599.98"))
                .status(PaymentStatus.REFUNDED).transactionId("TXN-ABC")
                .processedAt(LocalDateTime.now()).build();

        when(paymentRepository.findById(200L)).thenReturn(Optional.of(completedPayment));
        when(bookingRepository.save(any())).thenReturn(pendingBooking);
        when(flightRepository.save(any())).thenReturn(flight);
        when(paymentRepository.save(any())).thenReturn(refundedPayment);

        PaymentResponse result = service.refundPayment(200L);

        assertThat(result.status()).isEqualTo(PaymentStatus.REFUNDED);
        verify(bookingRepository).save(pendingBooking);
        verify(flightRepository).save(flight);
    }

    @Test
    void refundPayment_notCompleted_throwsBookingException() {
        completedPayment.setStatus(PaymentStatus.REFUNDED);
        when(paymentRepository.findById(200L)).thenReturn(Optional.of(completedPayment));

        assertThatThrownBy(() -> service.refundPayment(200L))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Only completed payments");
    }

    @Test
    void refundPayment_notFound_throwsResourceNotFoundException() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.refundPayment(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}