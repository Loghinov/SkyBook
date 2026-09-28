# Claude Code – Project Instructions

## Project
Flight ticket booking platform — Spring Boot 3.3.0, Java 21, PostgreSQL, Spring Security + JWT, Bootstrap 5 SPA.
Evolving from monolith toward microservices, one service at a time.

## MANDATORY: Session Bootstrap
At the **start of every conversation** you MUST:
1. USE SKILL `rosetta:load-project-context`
2. Apply HITL discipline from `rosetta:hitl`

Do NOT skip these steps even for small requests.

## Rules
- Never commit without explicit user request
- Never introduce dependencies, files, or abstractions not required by the current task
- When a new service needs a DB: STOP — guide user step by step, they set it up themselves
- H2 is for integration tests only — PostgreSQL for everything else
- Passwords must match: `^(?=.*[a-zA-Z])(?=.*\d).{8,}$`

## References
- How to run + test users → `docs/RUNBOOK.md`
- Slash commands per task type → `docs/WORKFLOWS.md`
- Architecture → `docs/ARCHITECTURE.md`
- Business context → `docs/CONTEXT.md`
