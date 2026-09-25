# Service Interface + Implementation

**Description**: Decouples contract from logic. Every domain service follows this split. Use when adding a new domain with business rules.

## Template
```java
// Interface
public interface <Resource>Service {
    <Resource>Response create(Create<Resource>Request request);
    <Resource>Response getById(Long id);
    List<<Resource>Response> getAll();
    void delete(Long id);
    // add domain-specific methods
}

// Implementation
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)   // default read-only; override per mutating method
public class <Resource>ServiceImpl implements <Resource>Service {

    private final <Resource>Repository <resource>Repository;
    // inject other repositories as needed

    @Override
    @Transactional
    public <Resource>Response create(Create<Resource>Request request) {
        // validate, build entity, save, return mapped response
        <Resource> entity = <Resource>.builder()
                // ... fields from request
                .build();
        return <Resource>Mapper.toResponse(<resource>Repository.save(entity));
    }

    @Override
    public <Resource>Response getById(Long id) {
        return <Resource>Mapper.toResponse(findById(id));
    }

    @Override
    public List<<Resource>Response> getAll() {
        return <resource>Repository.findAll().stream()
                .map(<Resource>Mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!<resource>Repository.existsById(id))
            throw new ResourceNotFoundException("<Resource> not found with id: " + id);
        <resource>Repository.deleteById(id);
    }

    // private finder used internally
    private <Resource> findById(Long id) {
        return <resource>Repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("<Resource> not found with id: " + id));
    }
}
```