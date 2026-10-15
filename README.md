# MiniShop Spring Boot

Learning project 4: a small shop and order API built with Java 17, Spring Boot, Spring Data JPA, and PostgreSQL.

## Current progress

- PostgreSQL Docker Compose configuration
- User, Category, Product, Order, and OrderItem entities
- Entity relationships and CREATED/CANCELLED order status
- Security starter dependency included; JWT authentication is not implemented yet

Repositories, DTOs, services, endpoints, and transactional order creation are next.

## Local setup

Create a `.env` file in the project root:

```properties
PG_URL=minishop
PG_USER=minishop
PG_PASSWORD=replace-with-your-local-password
```

`PG_URL` is the database name. Keep `.env` out of version control.

With Java 17 and Docker running, start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

Spring Boot's Docker Compose integration starts PostgreSQL. Host port 5432 must be available. Hibernate uses `ddl-auto=update` for local development.

## Planned behavior

JWT registration/login, category creation/listing, product CRUD with pagination, and user-owned orders. Order creation will validate stock, preserve purchase prices, calculate totals on the server, and save inventory changes inside one transaction.
