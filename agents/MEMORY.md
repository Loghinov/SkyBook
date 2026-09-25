# AGENT MEMORY
Generalized reusable lessons from agent sessions.
Root causes converted into preventive rules, not incident-specific notes.
Entries are h3 headers with [ACTIVE|RETIRED] status.
Content: brief, grep-friendly, MECE across sections.
Style: one-liner per entry, optional sub-bullets for context.
Keep template entries so that AI knows how to fill them in later on.

## Preventive Rules

### Read service interfaces AND their impls before writing business logic specs [ACTIVE]
Interface signatures alone do not reveal state transitions, validation, or DB side-effects.
- Example: BookingService interface shows `createBooking` but impl reveals seat decrement timing.

### Check SecurityConfig permit-all list before assuming any endpoint is protected [ACTIVE]
H2 console and static HTML files may be public even when the overall filter chain is authenticated.
- Always cross-reference SecurityConfig requestMatchers when documenting access control.

### Verify DataInitializer idempotency guard before describing seed data behavior [ACTIVE]
The `if (count > 0) return` guard means re-seeding is skipped on warm restarts; document this explicitly.

### <Generalized Preventive Rule> [ACTIVE]
[Root cause, Reasons, Problems]

## What Worked

### Parallel batch file reading reduces session latency on medium-sized codebases [ACTIVE]
Reading controllers, services, and config in parallel batches halves the round-trips needed before writing docs.

### Using ASSUMPTIONS.md as a forward-reference registry prevents stale docs [ACTIVE]
Recording unknowns with target file paths allows future agents to locate exactly where to update when assumptions are resolved.

### <Generalized What Worked> [ACTIVE]
[Root cause, Reasons, Problems]

## What Failed

### <Generalized What Failed> [ACTIVE]
[Hypothesis, Root cause, Reasons, Problems]

## Discoveries

### Spring Boot projects with H2 + DataInitializer have implicit demo-only constraints [ACTIVE]
H2 in-memory means all state resets on restart; DataInitializer idempotency guard only prevents duplicate seeds within a single run.
- Usage: always flag H2 as a TODO for production hardening.

### Dual search endpoints (POST + GET) on the same resource indicate frontend flexibility requirements [ACTIVE]
FlightController exposes both POST /search (body) and GET /search (query params) — likely driven by browser fetch vs form-GET usage.
- Usage: document both in API surface table; test both paths.

### <Generalized Discovery> [ACTIVE]
[Usage, Reasons, Problems]
