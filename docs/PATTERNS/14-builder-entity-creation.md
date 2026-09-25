# Builder-Based Entity Creation

**Description**: Entities are created exclusively via Lombok `@Builder` in service layer — never via setters or constructor arguments directly in create flows. Use whenever persisting a new entity from a request DTO.

## Template
```java
<Resource> entity = <Resource>.builder()
        .field1(request.field1())
        .field2(request.field2())
        .status(<StatusEnum>.INITIAL_STATE)   // set default status explicitly
        .relatedEntity(relatedEntity)          // resolved via repository.findById
        .build();
return <Resource>Mapper.toResponse(<resource>Repository.save(entity));
```
