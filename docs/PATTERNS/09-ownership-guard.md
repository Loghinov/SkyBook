# Ownership Guard

**Description**: Controller-level check ensuring a non-admin user can only access their own resources. Pattern repeats in BookingController, PaymentController, UserController. Use whenever a resource is user-scoped.

## Template
```java
@GetMapping("/{id}")
public ResponseEntity<<Resource>Response> getById(
        @PathVariable Long id, Authentication auth) {
    <Resource>Response resource = <resource>Service.getById(id);
    if (!securityUtils.isAdmin(auth)) {
        Long currentUserId = securityUtils.getCurrentUser(auth).getId();
        if (!currentUserId.equals(resource.userId())) {
            throw new UnauthorizedAccessException(
                    "You can only access your own <resource>s");
        }
    }
    return ResponseEntity.ok(resource);
}
```
