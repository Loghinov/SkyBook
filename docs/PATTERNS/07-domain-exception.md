# Domain Exception

**Description**: Unchecked `RuntimeException` subclass per error category. Thrown by services, caught by `GlobalExceptionHandler`. Use when adding a new error condition that maps to a distinct HTTP status.

## Template
```java
// Specific domain exception
public class <Domain>Exception extends RuntimeException {
    public <Domain>Exception(String message) {
        super(message);
    }
}

// Already present in project:
// ResourceNotFoundException  → 404
// BookingException           → 400
// UnauthorizedAccessException → 403
```
