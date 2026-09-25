# Rosetta Implementation Summary
This file is a brief and durable summary of the current implementation state.
It is intentionally concise and should not be used as a chronological work log.
For detailed change history, use git history and PRs instead of expanding this file.

## Baseline
- Spring Boot 3.3.0 / Java 21 / Maven flight booking demo application
- Full CRUD for Users, Flights, Bookings, Payments via REST API
- JWT-based stateless authentication with role-based access (USER / ADMIN)
- H2 in-memory database with seed data (DataInitializer)
- HTML5 + Bootstrap 5 SPA frontend (index.html, dashboard.html)

## Major Implemented Workstreams

### Authentication & Authorization: complete, 2024
- JWT login (/api/auth/login) and registration (/api/auth/register)
- Stateless Spring Security filter chain, BCrypt password encoding
- Role-based route protection (ADMIN vs USER) via SecurityConfig
- Controller-level ownership enforcement via SecurityUtils

### Flight Management: complete, 2024
- Create, retrieve, delete flights (admin-only create/delete)
- Dual search endpoints: POST /api/flights/search (body) and GET /api/flights/search (params)
- Seat tracking (availableSeats field on Flight entity)

### Booking Management: complete, 2024
- Create booking with passenger count and price calculation
- Cancel booking; status lifecycle PENDING → CONFIRMED → CANCELLED
- Per-user booking retrieval with ownership enforcement

### Payment Processing: complete, 2024
- Process payment for a booking (PENDING → COMPLETED)
- Refund payment (admin-only, COMPLETED → REFUNDED)
- Retrieve payment by booking ID

### User Profile: complete, 2024
- GET /api/auth/me (own profile)
- PUT /api/auth/profile (update name, phone)
- Admin user management: list users, create users, delete users

### Frontend SPA: complete, 2025-09
- index.html: login/register with animated SVG planes, frosted-glass card, star-dot background; on success → redirect to dashboard.html
- dashboard.html: full SPA — top-bar, secondary nav (Search/Trips/Profile/Help), hero banner, search card (auto-loads flights on open, 48px tall inputs, 1140px wide), flight result list + Book modal, My Trips (Pay/Cancel/Receipt), Profile editor, Help FAQ
- Amadeus live-search toggle shown only when amadeus-enabled=true

### Amadeus Integration: complete, 2025-09
- AmadeusService (@ConditionalOnProperty amadeus.enabled=true) wraps SDK v8.1.0
- GET /api/flights/live-search, GET /api/flights/amadeus-enabled added to FlightController
- amadeus.enabled=false by default; no credentials required to run the app

### Seed Data & Password Policy: updated, 2025-09
- DataInitializer now seeds 17 flights with dynamic future dates (LocalDateTime.now().plusDays(N))
- Routes include KIV (Chisinau) + European hubs: FRA, BER, BCN, LTN, AMS, VIE, CDG, IST, SVO, OTP, KBP
- Passwords updated to comply with new policy: admin1, pass1word
- RegisterRequest @Pattern: ^(?=.*[a-zA-Z])(?=.*\d).{8,}$ (min 8, letters+numbers required)
