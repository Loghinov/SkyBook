# Global Exception Handler

**Description**: `@RestControllerAdvice` mapping domain exceptions to RFC 7807 `ProblemDetail`. Centralises all HTTP error responses. Extend this class when adding a new exception type.

## Template
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(<Domain>Exception.class)
    public ProblemDetail handle<Domain>(<Domain>Exception ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.<STATUS>, ex.getMessage());
    }

    // Validation errors — already handled:
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors);
    }
}
```
