# Claude Code – Project Instructions

## MANDATORY: Session Bootstrap

At the **start of every conversation** in this project, you MUST:
1. USE SKILL `rosetta:load-project-context` — loads all project docs, patterns, memory, and implementation state before any work begins.
2. Apply HITL (human-in-the-loop) discipline from `rosetta:hitl` for every task — confirm intent before implementing, never assume.

Do NOT skip these steps even for small requests.

## Rosetta Workflow

This project uses **Rosetta v3.1.13** (plugin-mode). All workspace docs are in:
- `docs/` — CONTEXT.md, ARCHITECTURE.md, TECHSTACK.md, CODEMAP.md, PATTERNS/
- `agents/` — IMPLEMENTATION.md, MEMORY.md
- `gain.json` — SDLC configuration

### Slash commands to use for each task type:
| Task | Command |
|------|---------|
| Feature / bug fix / refactor | `/coding-flow <description>` |
| Extract or write requirements | `/requirements-authoring-flow <description>` |
| Modernization / migration | `/modernization-flow <description>` |
| Reload project context | `/rosetta:load-project-context` |

## Project Quick Reference

- **Stack**: Spring Boot 3.3.0, Java 21, H2 in-memory DB, Spring Security + JWT, Bootstrap 5 SPA
- **Run**: `/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn spring-boot:run`
- **URL**: http://localhost:8080
- **Test users**: `alice / pass1word`, `bob / pass1word`, `admin / admin1`
- **Amadeus**: disabled by default (`amadeus.enabled=false` in application.properties)

## Rules

- Never commit without explicit user request.
- Always restart the server after Java/config changes (static HTML changes take effect immediately after restart).
- Passwords must match: `^(?=.*[a-zA-Z])(?=.*\d).{8,}$` (min 8 chars, letters + numbers).
