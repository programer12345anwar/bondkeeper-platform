# BondKeeper

Personal Relationship Manager — help users maintain meaningful relationships with family, mentors, friends, and important contacts through intelligent reminders and relationship health tracking.

## Monorepo Structure

```
BondKeeper/
├── backend/     # Spring Boot 3.x REST API (Java 17)
├── frontend/    # Web application (Phase 2+)
├── mobile/      # Mobile application (Phase 2+)
├── docs/        # Architecture & database documentation
└── docker-compose.yml
```

## Tech Stack (Backend)

| Layer        | Technology                          |
|-------------|-------------------------------------|
| Runtime     | Java 17                             |
| Framework   | Spring Boot 3.2                     |
| Persistence | Spring Data JPA + PostgreSQL        |
| Migrations  | Flyway                              |
| Mapping     | MapStruct + Lombok                  |
| API Docs    | SpringDoc OpenAPI (Swagger UI)      |
| Container   | Docker + Docker Compose             |

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker & Docker Compose (optional)

### Run with Docker

```bash
docker compose up --build
```

- API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- PostgreSQL: localhost:5432 (user/pass/db: `bondkeeper`)

### Run locally (requires PostgreSQL)

```bash
cd backend
mvn spring-boot:run
```

### Demo credentials

| Email               | Password  |
|---------------------|-----------|
| demo@bondkeeper.app | password  |

## API Endpoints (v1)

| Resource         | Base Path                  |
|-----------------|----------------------------|
| Users           | `/api/v1/users`            |
| Categories      | `/api/v1/categories`       |
| Priority Levels | `/api/v1/priority-levels`  |
| Contacts        | `/api/v1/contacts`         |
| Interactions    | `/api/v1/interactions`     |
| Reminders       | `/api/v1/reminders`        |

## Architecture

Clean Architecture packages under `com.bondkeeper.backend`:

```
config → security → controller → service → repository → entity
                              ↘ dto / mapper / exception
scheduler / notification / util
```

See [docs/ER_DIAGRAM.md](docs/ER_DIAGRAM.md) for the complete database design.

## Phase Roadmap

- **Phase 1** (complete): Database + Backend foundation
- **Phase 2** (current): JWT authentication + secured core APIs
- **Phase 3**: Frontend web app, mobile app, push notifications

## Authentication (Phase 2)

All endpoints except auth register/login/refresh and Swagger require a Bearer token.

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Register new account |
| POST | `/api/v1/auth/login` | Login → access + refresh tokens |
| POST | `/api/v1/auth/refresh` | Refresh access token |
| POST | `/api/v1/auth/logout` | Revoke refresh tokens (requires auth) |
| GET | `/api/v1/users/me` | Get profile |
| PUT | `/api/v1/users/me` | Update profile |

**Postman collection:** [docs/postman/BondKeeper-API.postman_collection.json](docs/postman/BondKeeper-API.postman_collection.json)

### JWT configuration (env vars)

| Variable | Default | Description |
|----------|---------|-------------|
| `JWT_SECRET` | (dev hex key) | HMAC signing key (hex 64 chars or Base64) |
| `JWT_ACCESS_EXPIRATION_MS` | 900000 | Access token TTL (15 min) |
| `JWT_REFRESH_EXPIRATION_MS` | 604800000 | Refresh token TTL (7 days) |
