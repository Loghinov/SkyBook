# Stream-Map-toList

**Description**: All list-returning service methods convert entity lists to response DTOs via `.stream().map(Mapper::toResponse).toList()`. Use this idiom consistently — never return raw entity lists.

## Template
```java
public List<<Resource>Response> getAll() {
    return <resource>Repository.findAll().stream()
            .map(<Resource>Mapper::toResponse)
            .toList();
}

public List<<Resource>Response> getBy<Filter>(Type value) {
    return <resource>Repository.findBy<Filter>(value).stream()
            .map(<Resource>Mapper::toResponse)
            .toList();
}
```
