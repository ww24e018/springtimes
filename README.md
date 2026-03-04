# springtimes

A Spring Boot 4.0 backend for storing time-series datapoints. See [About.md](About.md) for design rationale.

## Running locally

### Option A — IntelliJ + local database

1. Start only the database:
   ```
   docker compose up db -d
   ```
2. In IntelliJ, open the run configuration for `SpringtimesApplication` and set the active profile to `local`.
   *(Run > Edit Configurations > Spring Boot tab > Active profiles: `local`)*
   This picks up `src/main/resources/application-local.properties`, which provides default credentials so no env vars are needed.
3. Run the application.

### Option B — Full Docker Compose

```
cp .env.example .env
docker compose up -d
```

Edit `.env` to set real values before exposing the service outside localhost.

## Smoke test

Once the app is running (either option), verify the auth endpoint:

```
curl -s -X POST http://localhost:8080/auth/token \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"changeme"}'
```

Expected response:
```json
{"token":"<jwt>"}
```

Use the returned token as a Bearer token for all other endpoints:
```
curl -s http://localhost:8080/categories \
  -H 'Authorization: Bearer <token>'
```

## Environment variables

| Variable | Description |
|----------|-------------|
| `JWT_SECRET` | Signing secret, minimum 32 characters |
| `APP_USERNAME` | Login username |
| `APP_PASSWORD` | Login password |
| `SPRING_DATASOURCE_URL` | JDBC URL for PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | DB user |
| `SPRING_DATASOURCE_PASSWORD` | DB password |
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed origins |

See `.env.example` for a complete template.
