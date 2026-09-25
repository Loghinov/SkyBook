# Flight Booking System

A Spring Boot 3.3.0 / Java 21 demo application for searching and booking flights, with JWT-based authentication and role-based access control.

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.8+

### Run
```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

### Access
- **Frontend SPA**: http://localhost:8080/index.html
- **H2 Console** (dev only): http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:flightdb` (or as configured in `application.properties`)
  - Username: `sa`, Password: *(empty)*

## Test Credentials

| Username | Password | Role |
|---|---|---|
| `admin` | `admin` | ADMIN |
| `alice` | `password123` | USER |
| `bob` | `password123` | USER |
| `carol` | `password123` | USER |

All accounts and seed data are created by `DataInitializer` on startup. Data is reset on every restart (H2 in-memory).

## API Overview

Base path: `/api`

| Area | Endpoints |
|---|---|
| Auth | POST /auth/login, POST /auth/register, GET /auth/me, PUT /auth/profile |
| Flights | GET/POST /flights, GET/DELETE /flights/{id}, POST/GET /flights/search |
| Bookings | POST /bookings, GET /bookings/{id}, GET /bookings/user/{userId}, PUT /bookings/{id}/cancel, GET /bookings (admin) |
| Payments | PUT /payments/{id}/refund (admin) |

All protected endpoints require `Authorization: Bearer <token>` header.

## Documentation

| Doc | Description |
|---|---|
| `docs/CONTEXT.md` | Business context, rules, stakeholders |
| `docs/ARCHITECTURE.md` | Technical layers, API surface, security model |
| `docs/CODEMAP.md` | File structure and package map |
| `docs/TECHSTACK.md` | Technology stack details |
| `docs/DEPENDENCIES.md` | Maven dependency inventory |
| `docs/ASSUMPTIONS.md` | Open assumptions from analysis |
| `docs/TODO.md` | Prioritized improvement backlog |
| `docs/PATTERNS/INDEX.md` | Code patterns and conventions |
| `agents/IMPLEMENTATION.md` | Current implementation state summary |

## Notes
- This is a demo application. H2 in-memory database resets on every restart.
- H2 console and default credentials are not suitable for production deployment.
- See `docs/TODO.md` for production-readiness items.
