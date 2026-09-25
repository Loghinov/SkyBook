# ARCHITECTURE.md
# What this doc is: Technical architecture of the Flight Booking System.
# What it contains: Layers, modules, key design decisions, security model, API surface.
# Style: Terse, grep-friendly. No business context — see CONTEXT.md. No file tree — see CODEMAP.md.

## Stack
- Spring Boot 3.3.0, Java 21, Maven
- Spring Security 6 + JWT (jjwt 0.12.3), stateless sessions
- Spring Data JPA + H2 in-memory database (dev/demo; no persistence across restarts)
- Lombok (compile-time boilerplate reduction)
- Frontend: HTML5 + Bootstrap 5 SPA (no build step, served as static resources)

## Layer Model
```
[Browser SPA]
      |  HTTP + JWT Bearer
[Controllers]       com.flightbooking.controller
      |
[Services]          com.flightbooking.service (interfaces + impl)
      |
[Repositories]      com.flightbooking.repository (Spring Data JPA)
      |
[Entities]          com.flightbooking.entity (JPA @Entity)
      |
[H2 in-memory DB]
```

## Package Responsibilities
| Package | Responsibility |
|---|---|
| `controller` | HTTP routing, request validation, ownership enforcement |
| `service` | Business logic interfaces + impls |
| `repository` | JPA repositories (CRUD + custom queries) |
| `entity` | JPA entities: User, Flight, Booking, Payment; enums: Role, BookingStatus, PaymentStatus |
| `dto/request` | Inbound request records (Java records, Bean Validation) |
| `dto/response` | Outbound response records (serialized to JSON) |
| `mapper` | Entity ↔ DTO conversion (UserMapper; others inline or via constructors) |
| `security` | SecurityConfig, JwtUtil, JwtAuthenticationFilter, UserDetailsServiceImpl, SecurityUtils |
| `exception` | BookingException, UnauthorizedAccessException, global exception handler |
| `config` | DataInitializer (ApplicationRunner — seeds DB on startup) |

## API Surface
| Method | Path | Auth | Notes |
|---|---|---|---|
| POST | /api/auth/login | public | Returns JWT + user metadata |
| POST | /api/auth/register | public | Creates USER-role account, returns JWT |
| GET | /api/auth/me | authenticated | Current user profile |
| PUT | /api/auth/profile | authenticated | Update own first/last name, phone |
| GET | /api/flights | authenticated | List all flights |
| GET | /api/flights/{id} | authenticated | Flight detail |
| GET | /api/flights/search | authenticated | GET-param search (origin, destination, date, passengers) |
| POST | /api/flights/search | authenticated | Body-based search |
| POST | /api/flights | ADMIN | Create flight |
| DELETE | /api/flights/{id} | ADMIN | Delete flight |
| POST | /api/bookings | authenticated | Create booking (own userId only) |
| GET | /api/bookings/{id} | authenticated | Get booking (own only; admin: any) |
| GET | /api/bookings/user/{userId} | authenticated | User's bookings (own only; admin: any) |
| PUT | /api/bookings/{id}/cancel | authenticated | Cancel booking (own only; admin: any) |
| GET | /api/bookings | ADMIN | All bookings |
| PUT | /api/payments/*/refund | ADMIN | Refund payment |

## Security Model
- Stateless JWT: no server-side session, token validated per request by `JwtAuthenticationFilter`
- Token carries: username, role claim, user ID
- Ownership enforcement: controller-layer checks via `SecurityUtils.getCurrentUser()` / `isAdmin()`
- BCrypt password encoding
- H2 console exposed at `/h2-console/**` (permitted; dev-only, not suitable for production)
- CSRF disabled (stateless JWT API)
- Frame options: sameOrigin (required for H2 console)

## Key Design Decisions
- **Service layer as interfaces**: enables future mocking/substitution; impls are in separate `impl` sub-packages
- **Ownership checks in controllers**: keeps service layer reusable without embedded security logic
- **Records for DTOs**: immutable, concise, zero-boilerplate with Java 21
- **H2 in-memory DB**: zero-config demo; all data lost on restart; swap to persistent DB for production
- **DataInitializer**: idempotent (skips if `userRepository.count() > 0`); runs every startup
- **No refresh tokens**: single-token stateless design; expiry strategy not visible in source (see JwtUtil)
- **Dual flight search endpoints**: POST `/search` (body) and GET `/search` (query params) — both active

## Frontend
- `index.html`: single-page app — login + register with animated background, frosted-glass card, redirects to dashboard after auth
- `dashboard.html`: post-login SPA — top-bar (logo + username + logout), secondary nav (Search/My Trips/My Profile/Help), hero + search card (auto-loads all flights on open), flight result list with Book button, My Trips with Pay/Cancel/Receipt, profile editor
- Static resources served directly by Spring Boot (no separate frontend server)

## Amadeus Integration (optional, disabled by default)
- `AmadeusService` — `@ConditionalOnProperty(amadeus.enabled=true)`; wraps Amadeus Java SDK v8.1.0
- `GET /api/flights/live-search` — proxies to Amadeus; requires `AmadeusService` bean present
- `GET /api/flights/amadeus-enabled` — returns bool; frontend uses it to show/hide live search toggle
- To activate: set `amadeus.enabled=true`, `amadeus.client-id`, `amadeus.client-secret` in `application.properties`

## Diagram: Request Flow (Auth-Protected)
```
Browser → [JwtAuthenticationFilter] → sets SecurityContext
       → [Controller] → ownership check via SecurityUtils
       → [Service] → business logic
       → [Repository] → JPA / H2
```
