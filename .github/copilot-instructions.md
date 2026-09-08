# Copilot Instructions — The Second Wave

## Project Overview
The Second Wave is an e-commerce web API for buying and selling **second-hand surf equipment** (boards, wetsuits, fins, leashes, etc...). It is a Spring Boot monolith exposing a REST API (JSON) plus a minimal Thymeleaf-rendered home page.

## Tech Stack
- **Java 17**, **Spring Boot 3.4.1**
- **Spring Web (MVC)** — REST controllers
- **Spring Data JPA** + **MySQL** (`mysql-connector-j`)
- **Flyway** (`flyway-core`, `flyway-mysql`) — DB migrations under `src/main/resources/db/migration`, versioned `V<N>__description.sql`
- **Spring Security** with **JWT** auth (`io.jsonwebtoken` / jjwt) — see `auth/` package
- **Lombok** — reduce boilerplate on entities/DTOs (`@Getter`, `@Setter`, `@Builder`, etc.)
- **MapStruct** — entity ↔ DTO mapping (`*Mapper` interfaces per package)
- **Bean Validation** (`spring-boot-starter-validation`)
- **springdoc-openapi** — Swagger UI/OpenAPI docs (check `/swagger-ui.html` or `/v3/api-docs` when the app is running for the authoritative, up-to-date API surface)
- **Thymeleaf** + `thymeleaf-extras-springsecurity6` — server-rendered home page only
- **Stripe** (`stripe-java`) — payment/checkout processing
- **spring-dotenv** — loads `.env` values (e.g. `JWT_SECRET`, `STRIPE_SECRET_KEY`, `STRIPE_WEBHOOK_SECRET_KEY`) into Spring properties

## Architecture & Conventions
- **Feature-based package structure** under `com.thesecondwave.store`, not layer-based. Each feature package (`products`, `carts`, `orders`, `payments`, `users`, `auth`, `admin`) contains its own controller, service, repository, entity, DTO(s), mapper, and exceptions together.
- **`common/`** holds cross-cutting pieces: `GlobalExceptionHandler`, `ErrorDto`, `LoggingFilter`, `HomeController`, and the `SecurityRules` interface.
- **Security rules are modular**: each feature package defines its own `*SecurityRules` class implementing `SecurityRules.configure(...)` to declare its own `authorizeHttpRequests` matchers (e.g. `AdminSecurityRules`, `AuthSecurityRules`, `CartSecurityRules`, `PaymentSecurityRules`, `ProductSecurityRules`, `UserSecurityRules`, `SwaggerSecurityRules`). When adding a new endpoint, add/update the matching `*SecurityRules` class rather than a central config — check `SecurityConfig`/`WebSecurityConfig` class that aggregates all `SecurityRules` beans for how they are wired together.
- **Repositories** are plain Spring Data JPA interfaces (`*Repository extends JpaRepository`), with custom query methods where needed (e.g. `findByCategoryId`, `findAllWithCategory`).
- **Controllers** are thin: they call repositories/services and mappers directly, use `ResponseEntity` for explicit status codes (400/404/201), and return DTOs, never entities.
- **Mappers**: use MapStruct interfaces (`XyzMapper`) with `toDto`, `toEntity`, and `update(dto, entity)` methods instead of hand-written mapping code.
- **Exceptions**: each feature defines its own not-found/conflict exceptions (e.g. `ProductNotFoundException`, `CartEmptyException`, `DuplicateUserException`), handled centrally by `GlobalExceptionHandler` returning `ErrorDto`.
- **Custom validation**: see `users/Lowercase` + `LowercaseValidator` for the pattern used for custom Bean Validation constraints.

## API Surface (base paths)
- `/auth` — login, refresh token, current user (`/auth/login`, `/auth/refresh`, `/auth/me`)
- `/users` — CRUD + `/users/{id}/change-password`
- `/products` — CRUD, list supports `?categoryId=`
- `/carts` — create cart, add/update/remove items
- `/orders` — list & get by id (read-only, created via checkout)
- `/checkout` — `POST /checkout` to start a Stripe checkout session, `POST /checkout/webhook` for Stripe webhook events
- `/admin` — <!-- TODO: sparse today (`/admin/hello`), fill in as admin features are added -->

Always check the running app Swagger UI / OpenAPI JSON for the authoritative, current list of endpoints, request/response shapes, and auth requirements before assuming behavior.

## Auth Model
- JWT-based: short-lived access token (15 min, `accessTokenExpiration: 900`) + refresh token (7 days, `refreshTokenExpiration: 604800`), configured in `application.yaml` under `spring.jwt`.
- `JwtAuthenticationFilter` validates the access token per request; `JwtService`/`Jwt` handle token creation/parsing.
- Roles are on `User`/`Role` — <!-- TODO: list actual roles (e.g. ROLE_USER, ROLE_ADMIN) and how they gate admin endpoints -->.

## Database & Migrations
- MySQL, managed exclusively through **Flyway** migrations in `src/main/resources/db/migration` (`V1`…`V5` so far: initial schema, cart tables, user roles, order tables, seed data).
- Never hand-edit the schema or use `hibernate.ddl-auto` to change structure — add a new versioned Flyway migration instead.
- Local dev DB config: `jdbc:mysql://localhost:8889/the-second-wave-db` (see the `flyway-maven-plugin` config in [pom.xml](pom.xml) and `application-dev.yaml`)

## Payments
- Stripe integration lives in `payments/`: `CheckoutService` builds a Stripe Checkout Session, `StripePaymentGateway` wraps the Stripe SDK behind the `PaymentGateway` interface, `CheckoutController` exposes `/checkout` and the `/checkout/webhook` endpoint for async payment status updates (`PaymentStatus` on orders).
- Stripe keys come from env vars `STRIPE_SECRET_KEY` / `STRIPE_WEBHOOK_SECRET_KEY` (via `spring-dotenv`, never commit real keys).

## Working Conventions
- Run/build with Maven: `./mvn clean install`, `./mvn test`, `./mvn spring-boot:run`,`./mvn flyway:migrate`.
- Profiles: `application.yaml` (common) + `application-dev.yaml` / `application-prod.yaml`; active profile defaults to `dev`.
- When adding a new feature package, mirror the existing structure: entity, repository, DTO(s), MapStruct mapper, service (if logic is non-trivial), controller, `*SecurityRules`, and feature-specific exceptions — then add a `GlobalExceptionHandler` case if a new exception type is introduced.
- Add a new Flyway migration file for any schema change; do not modify existing migration files once applied.