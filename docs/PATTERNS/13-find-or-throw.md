# Find-or-Throw

**Description**: Private helper in each service that wraps `repository.findById()` with `orElseThrow(ResourceNotFoundException)`. Eliminates repetition and ensures consistent 404 messages.

## Template
```java
// In <Resource>ServiceImpl:
private <Resource> findById(Long id) {
    return <resource>Repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "<Resource> not found with id: " + id));
}

// Usage in service methods:
<Resource> entity = findById(id);
```
