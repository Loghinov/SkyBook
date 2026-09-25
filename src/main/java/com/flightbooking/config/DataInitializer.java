package com.flightbooking.config;

import com.flightbooking.entity.*;
import com.flightbooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) return;

        // ── Admin ──────────────────────────────────────────────────────────
        userRepository.save(User.builder()
                .username("admin").firstName("Admin").lastName("User")
                .email("admin@system.local")
                .password(passwordEncoder.encode("admin1"))
                .role(Role.ADMIN).build());

        // ── Regular users ──────────────────────────────────────────────────
        User alice = userRepository.save(User.builder()
                .username("alice").firstName("Alice").lastName("Smith")
                .email("alice@example.com").phoneNumber("+1-212-555-0101")
                .password(passwordEncoder.encode("pass1word"))
                .role(Role.USER).build());
        User bob = userRepository.save(User.builder()
                .username("bob").firstName("Bob").lastName("Johnson")
                .email("bob@example.com").phoneNumber("+1-310-555-0202")
                .password(passwordEncoder.encode("pass1word"))
                .role(Role.USER).build());
        User carol = userRepository.save(User.builder()
                .username("carol").firstName("Carol").lastName("Williams")
                .email("carol@example.com").phoneNumber("+44-20-7946-0303")
                .password(passwordEncoder.encode("pass1word"))
                .role(Role.USER).build());

        LocalDateTime now = LocalDateTime.now();

        // ── Flights (all future dates, relative to startup) ────────────────
        Flight f1 = flightRepository.save(Flight.builder()
                .flightNumber("AA101").origin("JFK").destination("LAX")
                .departureTime(now.plusDays(3).withHour(8).withMinute(0))
                .arrivalTime(now.plusDays(3).withHour(11).withMinute(30))
                .price(new BigDecimal("299.99")).totalSeats(150).availableSeats(148).build());

        Flight f2 = flightRepository.save(Flight.builder()
                .flightNumber("DL205").origin("LAX").destination("ORD")
                .departureTime(now.plusDays(5).withHour(14).withMinute(0))
                .arrivalTime(now.plusDays(5).withHour(19).withMinute(45))
                .price(new BigDecimal("189.50")).totalSeats(180).availableSeats(179).build());

        Flight f3 = flightRepository.save(Flight.builder()
                .flightNumber("UA330").origin("ORD").destination("MIA")
                .departureTime(now.plusDays(7).withHour(10).withMinute(30))
                .arrivalTime(now.plusDays(7).withHour(14).withMinute(15))
                .price(new BigDecimal("249.00")).totalSeats(200).availableSeats(200).build());

        flightRepository.save(Flight.builder()
                .flightNumber("SW410").origin("MIA").destination("JFK")
                .departureTime(now.plusDays(10).withHour(7).withMinute(0))
                .arrivalTime(now.plusDays(10).withHour(9).withMinute(45))
                .price(new BigDecimal("179.99")).totalSeats(137).availableSeats(136).build());

        flightRepository.save(Flight.builder()
                .flightNumber("BA200").origin("JFK").destination("LHR")
                .departureTime(now.plusDays(4).withHour(22).withMinute(0))
                .arrivalTime(now.plusDays(5).withHour(10).withMinute(0))
                .price(new BigDecimal("749.00")).totalSeats(250).availableSeats(250).build());

        // ── European routes ────────────────────────────────────────────────
        flightRepository.save(Flight.builder()
                .flightNumber("LH340").origin("FRA").destination("KIV")
                .departureTime(now.plusDays(2).withHour(9).withMinute(15))
                .arrivalTime(now.plusDays(2).withHour(13).withMinute(5))
                .price(new BigDecimal("185.00")).totalSeats(120).availableSeats(115).build());

        flightRepository.save(Flight.builder()
                .flightNumber("KL881").origin("AMS").destination("KIV")
                .departureTime(now.plusDays(3).withHour(11).withMinute(30))
                .arrivalTime(now.plusDays(3).withHour(16).withMinute(10))
                .price(new BigDecimal("210.00")).totalSeats(100).availableSeats(98).build());

        flightRepository.save(Flight.builder()
                .flightNumber("MD101").origin("KIV").destination("FRA")
                .departureTime(now.plusDays(4).withHour(6).withMinute(45))
                .arrivalTime(now.plusDays(4).withHour(9).withMinute(50))
                .price(new BigDecimal("175.00")).totalSeats(90).availableSeats(87).build());

        flightRepository.save(Flight.builder()
                .flightNumber("MD102").origin("KIV").destination("BER")
                .departureTime(now.plusDays(6).withHour(14).withMinute(20))
                .arrivalTime(now.plusDays(6).withHour(17).withMinute(30))
                .price(new BigDecimal("158.00")).totalSeats(90).availableSeats(82).build());

        flightRepository.save(Flight.builder()
                .flightNumber("FR4421").origin("KIV").destination("BCN")
                .departureTime(now.plusDays(8).withHour(7).withMinute(0))
                .arrivalTime(now.plusDays(8).withHour(10).withMinute(40))
                .price(new BigDecimal("99.00")).totalSeats(189).availableSeats(160).build());

        flightRepository.save(Flight.builder()
                .flightNumber("W64422").origin("KIV").destination("LTN")
                .departureTime(now.plusDays(9).withHour(15).withMinute(30))
                .arrivalTime(now.plusDays(9).withHour(18).withMinute(15))
                .price(new BigDecimal("112.00")).totalSeats(180).availableSeats(150).build());

        flightRepository.save(Flight.builder()
                .flightNumber("TK495").origin("IST").destination("KIV")
                .departureTime(now.plusDays(5).withHour(20).withMinute(10))
                .arrivalTime(now.plusDays(5).withHour(22).withMinute(45))
                .price(new BigDecimal("145.00")).totalSeats(160).availableSeats(130).build());

        flightRepository.save(Flight.builder()
                .flightNumber("AF1234").origin("CDG").destination("KIV")
                .departureTime(now.plusDays(11).withHour(8).withMinute(0))
                .arrivalTime(now.plusDays(11).withHour(12).withMinute(30))
                .price(new BigDecimal("225.00")).totalSeats(140).availableSeats(120).build());

        flightRepository.save(Flight.builder()
                .flightNumber("OS767").origin("VIE").destination("KIV")
                .departureTime(now.plusDays(12).withHour(13).withMinute(50))
                .arrivalTime(now.plusDays(12).withHour(16).withMinute(35))
                .price(new BigDecimal("168.00")).totalSeats(110).availableSeats(95).build());

        flightRepository.save(Flight.builder()
                .flightNumber("SU274").origin("SVO").destination("KIV")
                .departureTime(now.plusDays(2).withHour(17).withMinute(0))
                .arrivalTime(now.plusDays(2).withHour(19).withMinute(10))
                .price(new BigDecimal("130.00")).totalSeats(120).availableSeats(110).build());

        flightRepository.save(Flight.builder()
                .flightNumber("RO471").origin("OTP").destination("KIV")
                .departureTime(now.plusDays(3).withHour(10).withMinute(15))
                .arrivalTime(now.plusDays(3).withHour(11).withMinute(30))
                .price(new BigDecimal("85.00")).totalSeats(80).availableSeats(75).build());

        flightRepository.save(Flight.builder()
                .flightNumber("PS752").origin("KBP").destination("KIV")
                .departureTime(now.plusDays(4).withHour(12).withMinute(30))
                .arrivalTime(now.plusDays(4).withHour(14).withMinute(0))
                .price(new BigDecimal("95.00")).totalSeats(100).availableSeats(88).build());

        // ── Bookings ───────────────────────────────────────────────────────
        Booking b1 = bookingRepository.save(Booking.builder()
                .user(alice).flight(f1).status(BookingStatus.CONFIRMED)
                .numberOfPassengers(2).totalPrice(f1.getPrice().multiply(new BigDecimal("2"))).build());
        paymentRepository.save(Payment.builder()
                .booking(b1).amount(b1.getTotalPrice()).status(PaymentStatus.COMPLETED)
                .transactionId("TXN-" + System.currentTimeMillis())
                .processedAt(LocalDateTime.now()).build());

        bookingRepository.save(Booking.builder()
                .user(bob).flight(f2).status(BookingStatus.PENDING)
                .numberOfPassengers(1).totalPrice(f2.getPrice()).build());

        bookingRepository.save(Booking.builder()
                .user(carol).flight(f3).status(BookingStatus.CANCELLED)
                .numberOfPassengers(3).totalPrice(f3.getPrice().multiply(new BigDecimal("3"))).build());
    }
}
