# CONTEXT.md
# What this doc is: Business context for the Flight Booking System.
# What it contains: Purpose, domain, stakeholders, and key business rules.
# Style: Bulleted, terse, no tech details. Reference ARCHITECTURE.md for tech.

## Purpose
- Demo / learning project — not intended for production deployment
- Demonstrates a full-stack Spring Boot REST API with JWT auth, JPA, and an HTML5 frontend
- Enable passengers to search, book, and pay for flights via a web interface
- Provide admins with flight inventory management and booking oversight

## Domain
- Core entities: User, Flight, Booking, Payment
- Booking lifecycle: PENDING → CONFIRMED (after payment) → CANCELLED
- Payment lifecycle: PENDING → COMPLETED | REFUNDED

## Stakeholders
- **Passenger (USER role)**: registers, searches flights, books seats, pays, cancels own bookings
- **Administrator (ADMIN role)**: manages flight inventory, views all bookings, processes refunds, manages users

## Key Business Rules
- A user may only create bookings for themselves (not on behalf of others)
- A user may only view or cancel their own bookings; admins see all
- Flight creation and deletion is admin-only
- Listing all bookings is admin-only; users query by user ID
- Payment refunds are admin-only
- Registration assigns USER role by default; no self-service admin promotion
- Duplicate email or username is rejected at registration
- Seat availability decremented at booking creation (PENDING); restored on cancellation; validated — throws 409 if insufficient
- Booking total price = flight price × number of passengers
- Payment success (PaymentService) auto-transitions booking PENDING → CONFIRMED
- Cancellation does not trigger refund automatically — admin initiates refund separately via PUT /api/payments/{id}/refund

## Seed Data (Development / Demo)
- Admin account: username `admin`, password `admin1`
- Regular users: `alice`, `bob`, `carol` — all with password `pass1word`
- 17 pre-seeded flights with future dates (relative to server startup): US routes (JFK/LAX/ORD/MIA/LHR) + European routes including KIV (Chisinau), FRA, BER, BCN, LTN, AMS, VIE, CDG, IST, SVO, OTP, KBP
- Pre-seeded bookings: alice/CONFIRMED, bob/PENDING, carol/CANCELLED

## Password Policy
- Registration requires: min 8 chars, at least one letter AND one number (pattern: `^(?=.*[a-zA-Z])(?=.*\d).{8,}$`)
- Case-insensitive; uppercase not required
