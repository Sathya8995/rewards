# Rewards Service

A production-oriented backend service for managing customer rewards, built with **Java, Spring Boot, Spring Security, PostgreSQL, JPA, Flyway, and JWT authentication**.

The project is being developed incrementally to demonstrate practical backend engineering concepts including REST API design, layered architecture, authentication and authorization, transactional business logic, database migrations, exception handling, testing, and production-ready engineering practices.

---

## Tech Stack

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT / JJWT
- BCrypt
- PostgreSQL
- Flyway
- Maven
- Lombok
- Jakarta Validation
- JUnit
- Mockito
- MockMvc
- OpenAPI / Swagger

---

## Architecture

The application follows a layered backend architecture:

Client
  |
  v
Spring Security Filter Chain
  |
  v
Controller
  |
  v
Service
  |
  v
Repository
  |
  v
PostgreSQL

For authenticated requests:

Client
  |
  | Authorization: Bearer <JWT>
  v
JwtAuthenticationFilter
  |
  v
JWT Signature & Expiration Validation
  |
  v
CustomUserDetailsService
  |
  v
SecurityContextHolder
  |
  v
Authorization
  |
  v
Controller
  |
  v
Service
  |
  v
Repository
  |
  v
PostgreSQL

---

## Current Features

### Reward Management

The service supports reward lifecycle operations including:

- Reward creation
- Reward retrieval
- Reward redemption
- Reward expiration validation
- Reward status management
- Transactional reward updates

Reward types currently include:

- FUEL
- GROCERY
- CASHBACK
- DISCOUNT

A reward contains information such as:

- Customer ID
- Reward type
- Points
- Status
- Issued timestamp
- Expiration timestamp
- Redemption timestamp
- Created/updated timestamps

---

## Reward Redemption

Reward redemption includes business-rule validation.

A reward cannot be redeemed when:

- The reward does not exist
- The reward has already been redeemed
- The reward has expired
- The reward is not in a redeemable status

Redemption operations are executed transactionally using Spring's `@Transactional`.

---

## Authentication & Authorization

The application uses **Spring Security with stateless JWT authentication**.

### Registration

Users can register through:

POST /api/auth/register

Passwords are never stored as plaintext.

They are hashed using:

BCryptPasswordEncoder

A BCrypt encoded password contains the algorithm version, work factor, salt, and hash output.

Newly registered users currently receive the:

ROLE_CUSTOMER

role.

---

## Login

Users authenticate through:

POST /api/auth/login

Example request:

{
  "username": "customer1",
  "password": "password123"
}

Authentication flow:

LoginRequest
  |
  v
AuthenticationManager
  |
  v
DaoAuthenticationProvider
  |
  v
CustomUserDetailsService
  |
  v
UserRepository
  |
  v
PostgreSQL
  |
  v
PasswordEncoder.matches()
  |
  v
Authenticated User
  |
  v
JWT Generation

A successful login returns an access token and refresh token.

---

## JWT Authentication

Access tokens are signed JWTs.

A JWT consists of:

HEADER.PAYLOAD.SIGNATURE

### Header

Describes the token and signing algorithm.

Example:

{
  "alg": "HS256",
  "typ": "JWT"
}

### Payload

Contains JWT claims such as:

{
  "sub": "customer1",
  "iat": "...",
  "exp": "..."
}

Standard claims used by the application include:

- `sub` - subject / username
- `iat` - issued at
- `exp` - expiration

### Signature

The signature protects the token against modification.

The application verifies the JWT signature using the configured signing key before trusting the token claims.

JWT payloads are signed, not encrypted, so sensitive information such as passwords must never be stored inside JWT claims.

---

## JWT Request Flow

Protected requests send:

Authorization: Bearer <access-token>

The request flows through:

Request
  |
  v
Tomcat
  |
  v
Servlet FilterChain
  |
  v
FilterChainProxy
  |
  v
SecurityFilterChain
  |
  v
JwtAuthenticationFilter
  |
  v
Extract Bearer Token
  |
  v
Verify Signature
  |
  v
Validate Expiration
  |
  v
Extract Username
  |
  v
Load UserDetails
  |
  v
Validate Token/User
  |
  v
Create Authentication
  |
  v
SecurityContextHolder
  |
  v
Authorization
  |
  v
DispatcherServlet
  |
  v
Controller
  |
  v
Service
  |
  v
Repository

The application uses:

SessionCreationPolicy.STATELESS

Spring Security therefore does not rely on an HTTP session to remember authenticated users between requests.

---

## Role-Based Authorization

The application currently supports:

ROLE_ADMIN
ROLE_CUSTOMER

Example authorization rules:

POST /api/rewards
  -> ADMIN

PATCH /api/rewards/{id}/redeem
  -> CUSTOMER

Authentication answers:

"Who is the user?"

Authorization answers:

"Is the authenticated user allowed to perform this operation?"

---

## Refresh Tokens

The application supports refresh tokens to avoid requiring users to submit their username and password every time an access token expires.

Current configuration:

Access Token
  -> Short-lived

Refresh Token
  -> Longer-lived

Refresh flow:

Access Token Expires
  |
  v
POST /api/auth/refresh
  |
  v
Refresh Token
  |
  v
Verify Signature
  |
  v
Verify Expiration
  |
  v
Verify token_type = refresh
  |
  v
Extract Username
  |
  v
Load UserDetails
  |
  v
Validate Token/User
  |
  v
Generate New Access Token

Refresh tokens include the custom claim:

token_type = refresh

This prevents ordinary access tokens from being used as refresh tokens.

The current implementation reuses the valid refresh token rather than rotating it.

Refresh-token rotation, persistence, revocation, and token-family reuse detection are planned security-hardening improvements.

---

## Exception Handling

The application uses centralized exception handling for application-level errors through:

@RestControllerAdvice

Examples include:

- Reward not found
- Invalid reward redemption
- Duplicate user registration
- Invalid refresh token

Security-filter-level JWT failures are handled separately because filters execute before the Spring MVC controller layer.

---

## Database Migrations

Database schema changes are managed using **Flyway**.

Migration files are located under:

src/main/resources/db/migration

Flyway provides:

- Version-controlled database schema changes
- Repeatable environment setup
- Migration validation
- Schema history tracking

Hibernate schema generation is not used as the source of truth for production schema management.

---

## Testing

The project includes multiple testing layers.

### Unit Tests

Service-layer business logic is tested using:

- JUnit
- Mockito

Examples include:

- Successful reward redemption
- Reward not found
- Already redeemed reward
- Expired reward

### Integration Tests

Integration tests verify application behavior with Spring and persistence infrastructure.

Technologies include:

@SpringBootTest
@Transactional

### Controller Tests

REST endpoints are tested using:

MockMvc

Controller tests validate:

- HTTP status codes
- Request/response behavior
- JSON response fields

The security test suite will be expanded after completion of the security module.

---

## Security Configuration

JWT secrets are externalized from source control.

Example:

security:
  jwt:
    secret: ${JWT_SECRET:}
    expiration-ms: 3600000
    refresh-expiration-ms: 604800000

The application validates required security configuration during startup.

Do not commit JWT secrets, database passwords, or other credentials to the repository.

---

## Running the Application

### Prerequisites

Install:

- Java
- Maven
- PostgreSQL

Configure the PostgreSQL datasource for your environment.

Set the JWT secret as an environment variable:

JWT_SECRET=<your-secure-secret>

The secret must be sufficiently strong for the configured HMAC signing algorithm.

Run the application:

./mvnw spring-boot:run

Or on Windows:

mvnw.cmd spring-boot:run

---

## API Overview

### Authentication

POST /api/auth/register

Registers a new customer.

POST /api/auth/login

Authenticates a user and returns JWT credentials.

POST /api/auth/refresh

Uses a valid refresh token to obtain a new access token.

### Rewards

POST /api/rewards

Creates a reward.

Required role:

ADMIN

PATCH /api/rewards/{id}/redeem

Redeems an eligible reward.

Required role:

CUSTOMER

Additional reward retrieval endpoints are available through the rewards API.

---

## Security Practices Implemented

- BCrypt password hashing
- Database-backed authentication
- Spring Security authorization
- Role-based access control
- Stateless authentication
- Signed JWT access tokens
- JWT expiration
- JWT signature verification
- Bearer-token authentication
- Refresh tokens
- Refresh-token type validation
- Externalized JWT secrets
- Request validation
- Security-sensitive logging practices

Passwords, JWT secrets, access tokens, refresh tokens, and complete Authorization headers should never be written to application logs.

---

## Planned Improvements

The project is being incrementally expanded toward an enterprise-style backend architecture.

Future areas include:

- Security test suite
- Standardized 401/403 responses
- Refresh-token rotation and revocation
- Logout/session management
- Redis caching
- Kafka/event-driven processing
- Fault tolerance and resilience patterns
- Scheduling
- Quartz Scheduler
- Observability and metrics
- Docker/containerization
- CI/CD improvements
- Cloud/AWS integration
- Performance testing and optimization

Features will be documented as they are implemented rather than represented as completed functionality in advance.

---

## Engineering Goals

This project is intended to demonstrate practical backend engineering skills beyond basic CRUD development, including:

- Layered application architecture
- REST API design
- Transaction management
- Database schema management
- Authentication and authorization
- Secure password handling
- JWT-based stateless security
- Exception handling
- Automated testing
- Production logging practices
- Performance and scalability considerations
- Distributed-system concepts

---

## Project Status

Current milestone:

Security foundation implemented with database-backed authentication, BCrypt password hashing, role-based authorization, JWT access tokens, and refresh-token support.

Development continues incrementally toward a production-oriented rewards platform.
