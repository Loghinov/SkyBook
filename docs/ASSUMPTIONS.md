# ASSUMPTIONS.md
# What this doc is: Tracked assumptions made during workspace analysis where source was absent or ambiguous.
# What it contains: Each assumption with confidence level and the target file to update when resolved.
# Style: Tabular + brief rationale. Resolve by updating the target file and removing the entry here.

## Format
Each entry: assumption | confidence | target file to update when resolved

---

## Resolved Assumptions

### JWT Expiration Strategy — RESOLVED
- **Fact**: `jwt.expiration=86400000` (24h). Configured via `application.properties`. No refresh token endpoint.
- **Resolved in**: `docs/ARCHITECTURE.md`

### Booking → Payment Link on Confirmation — RESOLVED
- **Fact**: `PaymentServiceImpl.processPayment()` sets `booking.setStatus(CONFIRMED)` on payment success. Coupling is in PaymentService, not BookingService.
- **Resolved in**: `docs/CONTEXT.md` business rules

### Seat Decrement Timing — RESOLVED
- **Fact**: `availableSeats` is decremented at booking creation (PENDING state). Cancellation restores seats. Seat availability validated before booking (throws `BookingException` if insufficient).
- **Resolved in**: `docs/CONTEXT.md` business rules

## Open Assumptions

### H2 Console Exposed in Production
- **Assumption**: H2 console is intentionally exposed only for development/demo; no environment-specific profile disables it in production.
- **Confidence**: High
- **Basis**: SecurityConfig permits `/h2-console/**` unconditionally; no `application-prod.properties` observed.
- **Target**: `docs/TODO.md` (security hardening item), `src/main/resources/application.properties`

### Admin Role Assignment
- **Assumption**: ADMIN role can only be assigned directly in the database or via DataInitializer; no admin-promotion API endpoint exists.
- **Confidence**: High
- **Basis**: Registration hardcodes `role(Role.USER)`; no PATCH/PUT role endpoint found in AuthController or a UserController.
- **Target**: `docs/CONTEXT.md` (business rules), `docs/ARCHITECTURE.md` (API surface)

### Flight Search Filtering Logic
- **Assumption**: `searchFlightsFlexible` (GET) applies partial matching — any null parameter is ignored (wildcard), allowing single-field search.
- **Confidence**: Medium
- **Basis**: Method signature accepts all params as `required = false`; FlightServiceImpl not read.
- **Target**: `src/main/java/com/flightbooking/service/impl/FlightServiceImpl.java`

### No Email Verification
- **Assumption**: Registration does not send a verification email; accounts are active immediately.
- **Confidence**: High
- **Basis**: No email sender dependency in pom.xml (DEPENDENCIES.md); register endpoint returns JWT immediately.
- **Target**: `docs/CONTEXT.md` if this changes

### Payment Method / Gateway — RESOLVED
- **Fact**: Payment is intentionally stubbed. This is a demo/learning project — no real gateway planned.
- **Resolved in**: `docs/CONTEXT.md`, `docs/TODO.md`
