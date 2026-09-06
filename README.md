# OnRide

A distributed ride-hailing backend built as a hobby project to learn distributed systems hands-on.

## Running it locally

**Prerequisites:** Java 26, Postgres, Redis (local).

1. `docker-compose up -d` — starts Kafka, Schema Registry, Kafka UI, Jaeger.
2. Start Redis locally.
3. Each service has a `.env.example` — copy to `.env`, fill it in, set those as env vars (or in your IDE run config).
4. Start `discovery-service` first, then the rest with `./gradlew bootRun` in each service directory.

All API requests go through the gateway: `http://localhost:8080`.

Kafka UI: `localhost:8090`. Jaeger UI: `localhost:16686`.

## User flow

**Rider:** sign up/login → `POST /rides/quotes` for a fare estimate → `POST /rides/book` to request a ride → `GET /rides/matches` to check match status.

**Driver:** sign up/login → onboard profile and vehicle (`/drivers/me`) → `POST /locations/ping` periodically to report location → `GET /rides/matches` to see offered rides → `POST /rides/{rideId}/accept` to accept one.

(All routed through the gateway under `/api/v1/...`.)

## Worth a look

- **H3 geospatial indexing** for batching resources into small cells across the world for efficient management.
- **Bipartite matching (Hungarian algorithm)** for batch ride-driver assignment and utilizing MCMF(Min Cost Max Flow) to get maximum matches per batch with minimum total cost.
- **Dynamic pricing** — surge pricing based on live driver-availability vs demand in an area. WIP.
- **Distributed tracing with OpenTelemetry** — traces a request across every service, including over gRPC and Kafka, viewable in Jaeger.
- **gRPC + Kafka** for inter-service communication — sync calls over gRPC, async events over Kafka with Avro.

## Tech stack

Java 26, Spring Boot 4, Spring Cloud Gateway, Eureka, PostgreSQL + Flyway, Redis, Kafka + Avro + Schema Registry, gRPC, Uber H3, OpenTelemetry + Jaeger, Spring Security + JWT, springdoc-openapi, Gradle (Kotlin DSL).

## API access

Swagger, aggregated behind the gateway:
```
http://localhost:8080/webjars/swagger-ui/index.html
```

## WIP

- Real-time surge pricing
- Metrics dashboards
- Deployment

## Future

- WebSocket layer for drivers and riders — continuous location pings, driver liveness checks, direct driver-rider connection for live location exchange, and push notifications for ride matches instead of polling
- Spring Cloud Config for centralized configuration — Eureka URL, Redis/Kafka endpoints, and OTel/Jaeger settings