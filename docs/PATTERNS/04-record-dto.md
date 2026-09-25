# Record DTO (Request / Response)

**Description**: Immutable Java records for API surface. Request records carry `@Valid` constraints; response records are plain projections. Use for every request/response pair.

## Template
```java
// Request DTO
public record Create<Resource>Request(
        @NotNull Long relatedId,
        @NotBlank String name,
        @Min(1) int quantity
        // add other validated fields
) {}

// Response DTO (no validation annotations)
public record <Resource>Response(
        Long id,
        Long relatedId,
        String relatedName,   // denormalized display field
        StatusEnum status,
        BigDecimal amount,
        LocalDateTime createdAt
) {}
```
