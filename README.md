# The Second Wave

The Second Wave is a Spring Boot web API for buying and selling second-hand surf equipment such as surfboards, wetsuits, fins, and leashes. It exposes a JSON REST API, a small Thymeleaf home page, and OpenAPI documentation for exploring the API.

## What You Will Learn

This project is useful if you are learning how a Java web application is assembled. It demonstrates:

- Spring Boot 3.4.1 with Java 17
- REST controllers and JSON request/response bodies
- Spring Data JPA repositories and MySQL persistence
- Flyway database migrations
- JWT-based authentication with access and refresh tokens
- Role-based authorization for administrators
- Bean Validation for incoming requests
- MapStruct and Lombok to reduce repetitive mapping and boilerplate code
- Stripe Checkout and webhook processing
- Thymeleaf for the small server-rendered home page
- OpenAPI documentation with Swagger UI

## Technology Stack

| Area | Technology |
| --- | --- |
| Language | Java 17 |
| Application framework | Spring Boot 3.4.1 |
| Web layer | Spring MVC |
| Database | MySQL 8 |
| Persistence | Spring Data JPA / Hibernate |
| Database changes | Flyway |
| Authentication | Spring Security and JWT |
| Payments | Stripe Checkout |
| API documentation | springdoc OpenAPI / Swagger UI |
| Server-rendered page | Thymeleaf |
| Build tool | Maven |

## Project Structure

The code is organized by feature instead of by technical layer:

```text
src/
├── main/
│   ├── java/com/thesecondwave/store/
│   │   ├── admin/       Admin endpoints and authorization rules
│   │   ├── auth/        Login, JWT creation, and security configuration
│   │   ├── carts/       Cart and cart-item operations
│   │   ├── common/      Shared error handling, home page, and security contracts
│   │   ├── orders/      Order queries and order DTOs
│   │   ├── payments/    Stripe Checkout and webhook handling
│   │   ├── products/    Product catalog operations
│   │   └── users/       Registration, profiles, and passwords
│   └── resources/
│       ├── application.yaml
│       ├── application-dev.yaml
│       ├── application-prod.yaml
│       ├── db/migration/ Flyway SQL migrations
│       └── templates/    Thymeleaf templates
└── test/
		└── java/             Spring Boot tests
```

Each feature generally keeps its controller, service, repository, entity, DTO, mapper, exceptions, and security rules together. This makes it easier to find all code related to one business capability.

## Prerequisites

Install the following before starting:

1. Java Development Kit 17.
2. Maven 3.9 or newer.
3. MySQL 8, running locally.
4. A Stripe account and test API keys if you want to exercise checkout.

You can check the first two prerequisites with:

```bash
java -version
mvn -version
```

## Database Setup

The development profile expects MySQL at:

```text
jdbc:mysql://localhost:8889/the-second-wave-db
```

It uses the following local credentials by default:

```text
Database: the-second-wave-db
Username: root
Password: root
Port: 8889
```

Port `8889` is common for local MySQL installations such as MAMP. If your MySQL server uses the usual port `3306`, update the datasource URL in `src/main/resources/application-dev.yaml` before starting the application.

The database itself can be created automatically because the connection URL contains `createDatabaseIfNotExist=true`. The application then runs the Flyway migrations from `src/main/resources/db/migration`:

- `V1__initial_migration.sql` creates the initial schema.
- `V2__create_cart_tables.sql` adds carts and cart items.
- `V3__add_role_to_users.sql` adds user roles.
- `V4__add_order_tables.sql` adds orders and order items.
- `V5__populate_database.sql` inserts product categories and sample products.

Do not edit a migration that has already been applied. Add a new versioned migration instead, for example `V6__add_product_images.sql`.

## Environment Variables

The application reads secrets from environment variables. Create a local `.env` file or export the variables in your shell. Never commit real secrets.

```dotenv
JWT_SECRET=replace-with-a-long-random-secret
STRIPE_SECRET_KEY=sk_test_replace_me
STRIPE_WEBHOOK_SECRET_KEY=whsec_replace_me
```

The JWT secret must be long enough for the configured HMAC key. A random value can be generated with:

```bash
openssl rand -base64 32
```

For local development, Stripe test keys are sufficient. Stripe webhook events must be forwarded to:

```text
http://localhost:8080/checkout/webhook
```

The configured website URL is `http://localhost:4242`; change it in the active profile if your client application runs elsewhere.

## Run the Application

From the project root:

```bash
mvn spring-boot:run
```

The API starts at:

```text
http://localhost:8080
```

The project currently activates the `dev` profile by default in `application.yaml`. To choose a profile explicitly:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

To build a runnable JAR:

```bash
mvn clean package
java -jar target/the-second-wave-1.0.0.jar
```

## Test the Project

Run the test suite with:

```bash
mvn test
```

The current test suite includes an application-context smoke test. It needs the configured MySQL database to be available because the Spring context initializes the JPA and Flyway components.

## API Documentation

Once the application is running, open:

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

The controllers include operation summaries, descriptions, response documentation, and feature tags. DTO fields are also described with schema examples.

## Authentication Workflow

### 1. Register a user

Registration is public:

```bash
curl -i -X POST http://localhost:8080/users \
	-H 'Content-Type: application/json' \
	-d '{
		"name": "Kelly Slater",
		"email": "kelly.slater@example.com",
		"password": "SurfsUp123"
	}'
```

The email must be valid and lowercase. Passwords must be between 6 and 25 characters.

### 2. Log in

```bash
curl -i -c cookies.txt -X POST http://localhost:8080/auth/login \
	-H 'Content-Type: application/json' \
	-d '{
		"email": "kelly.slater@example.com",
		"password": "SurfsUp123"
	}'
```

The response contains an access token. The server also sets an HTTP-only refresh-token cookie. Send the access token on protected requests:

```bash
curl http://localhost:8080/auth/me \
	-H 'Authorization: Bearer YOUR_ACCESS_TOKEN'
```

Access tokens expire after 15 minutes. Refresh tokens expire after 7 days and are sent to `POST /auth/refresh` as the `refreshToken` cookie:

```bash
curl -b cookies.txt -c cookies.txt -X POST http://localhost:8080/auth/refresh
```

### Local HTTPS note

The login controller marks the refresh-token cookie as `Secure`. Browsers only send a `Secure` cookie over HTTPS, so browser refresh-token testing over plain `http://localhost` may not work without a local HTTPS setup or a development-specific cookie configuration. The `curl` example is useful for checking the endpoint independently.

## API Routes

The following table describes the routes currently implemented by the controllers. Unless a route is explicitly public below, the security configuration requires authentication.

### Authentication

| Method | Route | Access | Purpose |
| --- | --- | --- | --- |
| `POST` | `/auth/login` | Public | Authenticate a user and issue an access token. |
| `POST` | `/auth/refresh` | Public | Issue a new access token using the refresh-token cookie. |
| `GET` | `/auth/me` | Authenticated | Return the current user. |

### Users

| Method | Route | Access | Purpose |
| --- | --- | --- | --- |
| `POST` | `/users` | Public | Register a new user. |
| `GET` | `/users` | Authenticated | List users. |
| `GET` | `/users/{id}` | Authenticated | Get one user. |
| `PUT` | `/users/{id}` | Authenticated | Update a user. |
| `DELETE` | `/users/{id}` | Authenticated | Delete a user. |
| `POST` | `/users/{id}/change-password` | Authenticated | Change a user password. |

### Products

| Method | Route | Access | Purpose |
| --- | --- | --- | --- |
| `GET` | `/products` | Public | List products; optionally use `?categoryId=2`. |
| `GET` | `/products/{id}` | Public | Get one product. |
| `POST` | `/products` | Admin | Create a product. |
| `PUT` | `/products/{id}` | Admin | Update a product. |
| `DELETE` | `/products/{id}` | Admin | Delete a product. |

### Carts

| Method | Route | Access | Purpose |
| --- | --- | --- | --- |
| `POST` | `/carts` | Authenticated | Create an empty cart. |
| `POST` | `/carts/{cartId}/items` | Authenticated | Add a product to a cart. |
| `GET` | `/carts/{cartId}` | Authenticated | View a cart. |
| `PUT` | `/carts/{cartId}/items/{productId}` | Authenticated | Change an item quantity. |
| `DELETE` | `/carts/{cartId}/items/{productId}` | Authenticated | Remove one item. |
| `DELETE` | `/carts/{cartId}/items` | Authenticated | Empty the cart. |

### Orders and checkout

| Method | Route | Access | Purpose |
| --- | --- | --- | --- |
| `GET` | `/orders` | Authenticated | List orders. |
| `GET` | `/orders/{orderId}` | Authenticated | Get one order. |
| `POST` | `/checkout` | Authenticated | Create a Stripe Checkout session from a cart. |
| `POST` | `/checkout/webhook` | Public | Receive Stripe payment events. Stripe signature validation is handled by the payment gateway. |

### Admin and documentation

| Method | Route | Access | Purpose |
| --- | --- | --- | --- |
| `GET` | `/admin/hello` | Admin | Test an admin-only route. |
| `GET` | `/swagger-ui.html` | Public | Open Swagger UI. |
| `GET` | `/swagger-ui/**` | Public | Swagger UI assets. |
| `GET` | `/v3/api-docs/**` | Public | OpenAPI JSON and related documentation. |

The `/` Thymeleaf home page is not explicitly allowed by the security rules, so it currently falls through to the default authenticated rule.

## Security Model

The application is stateless: it does not use server-side sessions for API authentication. A JWT authentication filter reads the access token from the `Authorization` header.

The available application roles are:

- `USER`
- `ADMIN`

Spring Security uses `hasRole("ADMIN")`, which expects the authenticated authority to be represented as `ROLE_ADMIN` internally. Product write operations and all `/admin/**` routes require this role.

The security rules are split by feature. When adding a route, update the relevant `*SecurityRules` class. Routes that do not match an explicit rule are protected by `anyRequest().authenticated()` in `SecurityConfig`.

## Database Commands

The Flyway Maven plugin is configured for the development database. With MySQL running, useful commands include:

```bash
mvn flyway:info
mvn flyway:validate
mvn flyway:migrate
```

The plugin also has `cleanDisabled=false`, which means `mvn flyway:clean` can delete the configured schema. Use that command only when you intentionally want to erase local data.

## Common Troubleshooting

### Cannot connect to MySQL

Confirm that MySQL is running, that it listens on port `8889`, and that the `root` password is `root`. Otherwise update `application-dev.yaml` and, if using Flyway Maven commands, the database settings in `pom.xml` as well.

### The application says `JWT_SECRET` is missing

Export the required variables or create a `.env` file in the project root before starting the application:

```bash
export JWT_SECRET="$(openssl rand -base64 32)"
export STRIPE_SECRET_KEY="sk_test_replace_me"
export STRIPE_WEBHOOK_SECRET_KEY="whsec_replace_me"
mvn spring-boot:run
```

### A request returns `401 Unauthorized`

Check that the route is protected and that the request contains a valid, unexpired access token:

```text
Authorization: Bearer <access-token>
```

### A request returns `403 Forbidden`

The token is valid, but the user does not have the required role. Check the admin routes and product mutation routes when working with `ROLE_ADMIN`.

### Stripe checkout fails

Verify both Stripe keys, confirm that the cart exists and is not empty, and check that the configured website URL is reachable by the client flow.

## Contributing Workflow

When changing the project:

1. Keep code inside the feature package that owns the behavior.
2. Add or update a Flyway migration for schema changes; do not modify applied migrations.
3. Add DTO validation for new request fields.
4. Update the feature security rules for every new endpoint.
5. Add OpenAPI annotations and DTO schema details for public API changes.
6. Run `mvn test` before submitting the change.

## License

See [LICENSE](LICENSE) for the project license.
