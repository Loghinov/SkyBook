package com.flightbooking.exception;

public class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }
}