# Flight Booking System

A Spring Boot 3.3.0 / Java 21 demo application for searching and booking flights, with JWT-based authentication and role-based access control. Runs locally via Docker Compose (PostgreSQL + Spring Boot + nginx with HTTPS).

## Quick Start

### Prerequisites
- Docker
- Docker Compose

### First-time setup

```bash
# 1. Copy environment template and fill in real values
cp .env.example .env

# 2. Generate self-signed SSL certificate for local HTTPS
sh nginx/generate-certs.sh

# 3. Build and start all services
docker compose up --build
```

Application is available at **https://localhost**.

> The browser will show a "Your connection is not private" warning — this is expected for self-signed certificates. Accept and proceed.

### Everyday commands

```bash
docker compose up          # start (no rebuild)
docker compose up --build  # rebuild after code changes
docker compose down        # stop and remove containers
docker compose down -v     # stop + delete Postgres data
```

## Test Credentials

| Username | Password    | Role  |
|----------|-------------|-------|
| `admin`  | `admin1`    | ADMIN |
| `alice`  | `pass1word` | USER  |
| `bob`    | `pass1word` | USER  |
| `carol`  | `pass1word` | USER  |

Seed data is inserted by `DataInitializer` on first startup. Postgres data persists across restarts (in a Docker volume).

## Running tests

Unit tests (Mockito-based, no database required):
```bash
mvn test
```

Tests also run automatically in GitHub Actions on push and pull request.

## API Overview

Base path: `/api`

| Area     | Endpoints                                                                                                            |
|----------|----------------------------------------------------------------------------------------------------------------------|
| Auth     | POST /auth/login, POST /auth/register, GET /auth/me, PUT /auth/profile                                               |
| Flights  | GET/POST /flights, GET/DELETE /flights/{id}, POST/GET /flights/search                                                |
| Bookings | POST /bookings, GET /bookings/{id}, GET /bookings/user/{userId}, PUT /bookings/{id}/cancel, GET /bookings (admin)    |
| Payments | PUT /payments/{id}/refund (admin)                                                                                    |

All protected endpoints require `Authorization: Bearer <token>` header.

## Documentation

| Doc                          | Description                                        |
|------------------------------|----------------------------------------------------|
| `docs/CONTEXT.md`            | Business context, rules, stakeholders              |
| `docs/ARCHITECTURE.md`       | Technical layers, API surface, security model      |
| `docs/CODEMAP.md`            | File structure and package map                     |
| `docs/TECHSTACK.md`          | Technology stack details                           |
| `docs/DEPENDENCIES.md`       | Maven dependency inventory                         |
| `docs/ASSUMPTIONS.md`        | Open assumptions from analysis                     |
| `docs/TODO.md`               | Prioritized improvement backlog                    |
| `docs/PATTERNS/INDEX.md`     | Code patterns and conventions                      |
| `agents/IMPLEMENTATION.md`   | Current implementation state summary               |

## Notes
- Demo / learning project — not production-hardened.
- Self-signed certificates are local-only; use Let's Encrypt or similar for public deployment.
- See `docs/TODO.md` for the roadmap of hardening items.