# Transactional Read/Write Split

**Description**: Service class annotated `@Transactional(readOnly = true)` by default; mutating methods override with `@Transactional`. Reduces lock contention and flags intent clearly.

## Template
```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)   // <-- default for all methods
public class <Resource>ServiceImpl implements <Resource>Service {

    // Read methods inherit class-level readOnly — no annotation needed

    @Override
    @Transactional                // <-- override for writes
    public <Resource>Response create(Create<Resource>Request request) {
        // ...
    }

    @Override
    @Transactional
    public <Resource>Response update(Long id, ...) {
        // ...
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // ...
    }
}
```
