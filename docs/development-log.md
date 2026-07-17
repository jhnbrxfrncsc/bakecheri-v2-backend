# Development Log

# <center> DAY ONE </center>

## Goal

Establish the project foundation and implement the first working API endpoint (`GET /products`) using Java Servlets, JDBC, PostgreSQL, and Tomcat.

---

## Completed

### Project Setup

- Initialized Maven project
- Initialized Git repository
- Configured `.gitignore`
- Added README.md
- Added CHANGELOG.md

### Project Structure

Created packages:

- config
- dao
- dao.impl
- dto
- entity
- exception
- filter
- listener
- service
- service.impl
- servlet
- util

### Documentation

Created documentation:

- architecture.md
- api.md
- decisions.md
- deployment.md
- development-log.md

### Build & Deployment

Created deployment scripts:

- build.bat
- deploy.bat
- stop_tomcat.bat
- start_tomcat.bat
- clean_tomcat.bat
- run.bat

### Database

Created:

- schema
- seed.sql

Configured:

- PostgreSQL
- HikariCP
- application.properties

### Configuration

Implemented:

- ApplicationProperties
- DatabaseConfig
- JsonUtil
- HttpResponseUtil

### Domain Layer

Implemented:

- Product Entity

### Persistence Layer

Implemented:

- ProductDAO
- ProductDAOImpl

Features:

- findAll()
- findById()

### Service Layer

Implemented:

- ProductService
- ProductServiceImpl

### Web Layer

Implemented:

- ProductServlet

Endpoints:

- GET /products
- GET /products?id={id}

Verified using Postman.

---

## Lessons Learned

### Java

- Why `BigDecimal` is preferred over `double` for monetary values.
- Mapping PostgreSQL types to Java types.
- Entity equality using `equals()` and `hashCode()`.
- Difference between `getClass()` and `instanceof`.

### JDBC

- Proper use of `PreparedStatement`
- try-with-resources
- Mapping `ResultSet` into entities

### Architecture

Implemented a layered architecture:

Servlet

↓

Service

↓

DAO

↓

DB

Also practiced constructor-based dependency injection without a framework.

---

## Problems Encountered
### 1) Issue

LocalDateTime serialization failed.

### 1.1) Error
Java 8 date/time type `java.time.LocalDateTime` not supported by default.

### 1.2) Resolution

Added:

- jackson-datatype-jsr310

Created:

- JsonUtil

Configured:

- JavaTimeModule
- WRITE_DATES_AS_TIMESTAMPS disabled

### 1.3) Learned

Jackson does not automatically support Java 8 date/time types. Additional modules must be registered for proper serialization.

---

### 2) Issue

The original deployment batch script became difficult to debug because it performed multiple responsibilities (build, deploy, clean, start) in a single file.

### 2.1) Resolution

Refactored the deployment pipeline into modular scripts:

- build.bat
- stop_tomcat.bat
- clean_tomcat.bat
- deploy.bat
- start_tomcat.bat
- run.bat

### 2.2) Learned

Separating deployment steps makes each script independently testable and easier to maintain. This also mirrors how CI/CD pipelines (e.g., Jenkins or GitHub Actions) break deployment into discrete stages.



---
---

# Development Log

---

# <center>DAY TWO</center>

## Completed

### Product Module
- Completed Product DAO layer
    - `findAll()`
    - `findById()`
    - `findPopular()`
    - `findByCategory()`
    - `search()`
- Implemented reusable SQL constants to reduce duplicated SQL statements.
- Added product search functionality using:
    - product name
    - description
    - category
- Implemented case-insensitive searching using `LOWER(...)` and `LIKE`.

---

### DTO Layer
- Created `ProductDTO`.
- Removed database-specific fields from API responses:
    - `stockQuantity`
    - `createdAt`
    - `updatedAt`
- Established separation between Entity and API model.

---

### Mapper Layer
- Added `ProductMapper`.
- Implemented:
    - `Product -> ProductDTO`
    - `List<Product> -> List<ProductDTO>`

---

### Service Layer
- Updated `ProductService` to return DTOs instead of Entities.
- Added support for:
    - Find All
    - Find By Id
    - Popular Products
    - Category Filter
    - Product Search

---

### Servlet Layer
- Refactored `ProductServlet`.
- Introduced handler methods:
    - `handleFindAll()`
    - `handleFindById()`
    - `handleFindPopular()`
    - `handleFindByCategory()`
    - `handleSearch()`
- Established query parameter priority:

```
id
↓
popular
↓
category
↓
search
↓
findAll
```

Supported endpoints:

```
GET /products
GET /products?id=1
GET /products?popular=true
GET /products?category=Cake
GET /products?search=chocolate
```

---

### API Infrastructure
- Added generic `ApiResponse<T>`.
- Standardized successful responses.
- Standardized error responses.
- Refactored `HttpResponseUtil` to use `ApiResponse`.

---

### Exception Hierarchy
Created reusable API exception classes.

```
ApiException
├── BadRequestException
├── ValidationException
├── ResourceNotFoundException
└── DatabaseException
```

Prepared the project for centralized exception handling.

---

## Learned

### DTO Pattern
- API responses should not expose persistence entities directly.
- DTOs provide flexibility and better API design.

### Mapper Pattern
- Separating mapping logic keeps Service and Servlet layers clean.

### Layered Architecture
- DAO
- Service
- Mapper
- DTO
- Servlet

Each layer has a single responsibility.

### Generic API Responses
- Consistent response structures simplify frontend integration.

### Custom Exception Hierarchy
- Separating exception types improves readability and future maintainability.

---

## Problems Encountered

### 1. ProductServlet became difficult to read

#### Cause

As additional query parameters were introduced (`id`, `popular`, `category`, `search`), a single `doGet()` method became increasingly difficult to maintain.

#### Resolution

Extracted each operation into dedicated private handler methods.

```
handleFindAll()
handleFindById()
handleFindPopular()
handleFindByCategory()
handleSearch()
```

Also established a fixed query parameter precedence.

#### Learned

Large controller methods should coordinate requests, not contain all business logic.

---

## Next

- Introduce validation utilities.
- Centralize exception handling.
- Refactor DAO exception handling.
- Implement Product CRUD:
    - POST
    - PUT
    - DELETE
- Integrate React frontend with Servlet backend.



