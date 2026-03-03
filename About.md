# Project 'springtimes'

A Spring Boot backend for storing "time series data".

Specifically numeric (decimal) datapoints for specific instants tagged with a category (string).

Originally intended for (potential) personal use, but mainly as a research prototype / point of comparison / portfolio piece.

## Base design — rationale / simplicity of bootstrapping:
- postgres for data storage - backend and db connected to and exposed by docker-compose.yaml
- auth via JWT and without granular access controls
- an easy way to create a valid JWT: single `POST /auth/token` endpoint, credentials (username/password) stored as ENV vars, signed with a configurable secret
- Config of secrets via ENV var (the docker-compose will have to pass them through)
- UUIDs or similar for database primary keys and such
- category is a first-class entity (foreign key), not a free string per datapoint — enables fast lookup and validates category existence at insertion time
- CORS configured via ENV var (`CORS_ALLOWED_ORIGINS`) for SPA browser client use
- simple CRUD interfaces where appropriate
- endpoints to get datapoints for chunked time periods (like: years, months) — e.g. `GET /categories/{id}/datapoints?from=...&to=...`
- aggregation is explicitly out of scope: the backend returns raw datapoints, consumers decide what to compute
- Lombok for reducing boilerplate in Java entities and services
