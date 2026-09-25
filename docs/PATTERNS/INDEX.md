# PATTERNS Index

## 01-rest-controller - @RestController class with CRUD endpoints returning ResponseEntity
## 02-service-interface-impl - Service interface + @Service impl split for every domain
## 03-jpa-entity - @Entity with Lombok @Builder and @PrePersist timestamp
## 04-record-dto - Java records for request (with @Valid) and response DTOs
## 05-static-mapper - Utility class with private constructor and static toResponse method
## 06-jpa-repository - JpaRepository with derived finders and optional @Query
## 07-domain-exception - RuntimeException subclass per error category
## 08-global-exception-handler - @RestControllerAdvice mapping exceptions to ProblemDetail
## 09-ownership-guard - Controller check restricting non-admin users to their own resources
## 10-jwt-auth-flow - Stateless JWT login/register/filter authentication flow
## 11-security-config - SecurityFilterChain with stateless session, JWT filter, role rules
## 12-transactional-readwrite - Class-level readOnly=true, method-level @Transactional for writes
## 13-find-or-throw - Private findById helper throwing ResourceNotFoundException on miss
## 14-builder-entity-creation - Lombok builder for new entity creation from request DTO in service
## 15-stream-map-tolist - .stream().map(Mapper::toResponse).toList() for all list projections
