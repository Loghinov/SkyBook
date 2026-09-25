# TODO.md
# What this doc is: Tracked improvements, risks, and gaps found during workspace analysis.
# What it contains: Prioritized items with location and rationale.
# Style: Each header = ## [priority] [when] [what] [where]. Body = details + rationale.

---

## HIGH if-deployed Remove H2 in-memory DB `src/main/resources/application.properties`
Swap H2 for a persistent RDBMS (PostgreSQL, MySQL). All data is lost on every restart.
Not urgent — project is demo/local-only. Gate with `spring.profiles: dev` when deploying.

## HIGH if-deployed Harden H2 console exposure `src/main/java/com/flightbooking/security/SecurityConfig.java`
`/h2-console/**` is permitted for all environments. Gate behind a `dev` Spring profile before any deployment.

## LOW eventually Replace stub payment processing `src/main/java/com/flightbooking/service/impl/PaymentServiceImpl.java`
Payment is intentionally stubbed (demo project). If ever productionised, integrate a real gateway (Stripe, etc.).

## MEDIUM eventually Add JWT refresh token endpoint `src/main/java/com/flightbooking/controller/AuthController.java`
No refresh token. Users must re-login after 24h token expiry. Add POST /api/auth/refresh if needed.

## MEDIUM soon Enforce Booking → Payment transactional coupling `src/main/java/com/flightbooking/service/impl/BookingServiceImpl.java`
Unclear whether CONFIRMED status requires payment completion as a precondition.
Document and enforce: either (a) status auto-transitions to CONFIRMED on payment, or (b) they remain independent.
See ASSUMPTIONS.md: "Booking → Payment Link on Confirmation".

## MEDIUM soon Add cancellation → auto-refund policy `src/main/java/com/flightbooking/service/impl/BookingServiceImpl.java`
Cancellation does not trigger a refund. Admin must manually initiate PUT /api/payments/*/refund.
Define and implement a configurable refund policy (full refund, partial, no refund by window).

## MEDIUM soon Write integration and unit tests `src/test/java/com/flightbooking/`
No test classes observed in the source tree. Add:
- Unit tests for service impls (MockitoExtension)
- Integration tests for controllers (MockMvc + @SpringBootTest)
- Security tests: verify ownership enforcement and role-gated endpoints

## MEDIUM soon Add logging and structured error responses `src/main/java/com/flightbooking/exception/`
No SLF4J/Logback configuration observed. Add request/response logging for audit trail.
Standardize error response body (code, message, timestamp) across all exception types.

## LOW eventually Add admin role promotion endpoint `src/main/java/com/flightbooking/controller/`
Currently only DataInitializer can create an ADMIN user. Add a PATCH /api/users/{id}/role endpoint (ADMIN-only) for role management without DB access.

## LOW eventually Externalize JWT secret and expiry `src/main/resources/application.properties`
JWT secret is likely hardcoded in JwtUtil or application.properties. Move to environment variables / secrets manager before production deployment.

## LOW eventually Paginate list endpoints `src/main/java/com/flightbooking/controller/`
GET /api/flights, GET /api/bookings, and GET /api/bookings/user/{userId} return unbounded lists.
Add Spring Data Pageable support to all list endpoints.

## LOW eventually Validate flight dates on creation `src/main/java/com/flightbooking/service/impl/FlightServiceImpl.java`
No validation that departureTime < arrivalTime or that departure is in the future.
Add @Future constraint and cross-field validation in CreateFlightRequest or service layer.
