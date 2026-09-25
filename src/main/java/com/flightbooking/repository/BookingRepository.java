package com.flightbooking.repository;

import com.flightbooking.entity.Booking;
import com.flightbooking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByFlightIdAndStatus(Long flightId, BookingStatus status);
}