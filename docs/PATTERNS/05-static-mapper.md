# Static Mapper

**Description**: Utility class with private constructor and static `toResponse` method. Converts entity to response DTO. Use whenever a controller or service needs entity-to-DTO conversion.

## Template
```java
public class <Resource>Mapper {
    private <Resource>Mapper() {}

    public static <Resource>Response toResponse(<Resource> entity) {
        return new <Resource>Response(
                entity.getId(),
                entity.getRelated().getId(),
                entity.getRelated().getName(),   // denormalized display field
                entity.getStatus(),
                entity.getAmount(),
                entity.getCreatedAt()
        );
    }
}
```
