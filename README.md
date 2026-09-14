# OnRide

A ride-hailing backend built with Spring Boot microservices, Kafka, gRPC, and H3 geospatial matching, running on AWS EKS.

## Live demo

**Swagger:** https://unable-absolutely-packs-blogging.trycloudflare.com/webjars/swagger-ui/index.html
**Jaeger:** https://unable-absolutely-packs-blogging.trycloudflare.com/jaeger

Deploys are automated via GitHub Actions on every push to `main`.

## Design

- **Matching** — ride requests and available drivers are batched on a fixed interval and assigned using the Hungarian algorithm.
- **Geospatial lookups** — H3 hexagonal indexing for nearby-driver queries.
- **Inter-service communication** — gRPC for synchronous calls, Kafka + Avro + Schema Registry for async events.
- **Tracing** — requests are traced across HTTP, gRPC, and Kafka via OpenTelemetry, viewable in Jaeger.
- **Deployment** — Kubernetes on AWS EKS, nginx Ingress, native k8s service discovery (no Eureka in the cluster).

## Tech stack

Java 26 · Spring Boot 4 · Spring Cloud Gateway · PostgreSQL + Flyway · Redis · Kafka + Avro · gRPC · Uber H3 · OpenTelemetry + Jaeger · JWT · Docker (Jib) · Kubernetes (EKS) + nginx Ingress · GitHub Actions

<details>
<summary>Architecture &amp; user flow</summary>

`api-gateway` → `auth-service`, `location-service`, `ride-service`, `matching-service`. `discovery-service` is local-dev only (no Eureka in k8s).

**Rider:** signup/login → `POST /rides/quotes` → `POST /rides/book` → `GET /rides/matches`.
**Driver:** signup/login → `/drivers/me` → `POST /locations/ping` → `GET /rides/matches` → `POST /rides/{id}/accept`.

All under `/api/v1/...` via the gateway.

</details>

<details>
<summary>Running it locally</summary>

Prereqs: Java 26, Postgres, Redis.

1. `docker-compose up -d` — Kafka, Schema Registry, Kafka UI, Jaeger
2. Start Redis
3. Copy each service's `.env.example` → `.env`, fill in
4. Start `discovery-service`, then `./gradlew bootRun` per service

Gateway: `localhost:8080` · Swagger: `/webjars/swagger-ui/index.html` · Kafka UI: `:8090` · Jaeger: `:16686`

</details>

## Future scope

- Real-time surge pricing
- Metrics dashboards (Prometheus + Grafana)
- WebSocket layer for live location/match updates
- Spring Cloud Config