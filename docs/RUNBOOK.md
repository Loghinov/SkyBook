# RUNBOOK

## Run locally
Requires environment variables set before starting:
```
DB_URL=jdbc:postgresql://localhost:5432/flightdb
DB_USERNAME=flight_user
DB_PASSWORD=changeme
JWT_SECRET=your-secret-key-min-32-chars
```
```bash
mvn spring-boot:run
```
App available at: http://localhost:8080

## Test users (seed data)
| Username | Password | Role |
|---|---|---|
| admin | admin1 | ADMIN |
| alice | pass1word | USER |
| bob | pass1word | USER |
| carol | pass1word | USER |

## Run tests
```bash
mvn test
# uses H2 in-memory via src/test/resources/application-test.properties
```

## Password policy
`^(?=.*[a-zA-Z])(?=.*\d).{8,}$` — min 8 chars, at least one letter + one number
