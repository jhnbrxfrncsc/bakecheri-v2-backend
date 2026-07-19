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

---
---

# Development Log

# DAY THREE

## Completed

### Product API Enhancements
- Added `create(Product)` to `ProductDAO`
- Started implementing `update(Product)` workflow
- Added SQL INSERT statement with generated key retrieval using:
  - `Statement.RETURN_GENERATED_KEYS`
- Improved database logging for create operations
- Added custom `DatabaseException` handling for failed inserts

---

### Request / Response DTO Separation

Refactored DTO structure to separate API requests from API responses.

Added:

```
dto/
├── request/
│   ├── CreateProductRequest.java
│   └── UpdateProductRequest.java
│
└── response/
    └── ProductResponse.java
```

This separates:

- Incoming client requests
- Database entities
- Outgoing API responses

instead of using a single DTO for every layer.

---

### Product Mapper Improvements

Extended `ProductMapper` to support:

- Request DTO → Entity
- Entity → Response DTO

This keeps mapping logic centralized and prevents duplication throughout the application.

---

### Product Service Refactoring

Updated ProductService and ProductServiceImpl to use the new request/response DTOs.

Implemented:

- create(CreateProductRequest)
- update(UpdateProductRequest)

Added a reusable private helper:

- getSingleProduct(Long id)

to centralize product existence checking.

---

### Partial Update Utility

Created:

```
util/
└── UpdateUtil.java
```

Provides reusable generic methods for updating entity fields only when values actually changed.

Supports:

- Generic object comparison
- String comparison (null-safe and blank-safe)

This reduces repetitive update logic inside service classes.

---

### Validation Improvements

Created validation package structure:

```
validation/
├── ProductValidator.java
├── ValidationUtils.java
└── ValidationResult.java
```

Implemented initial validation methods including:

- required field validation
- blank string validation

Prepared validator structure for POST and PUT endpoints.

---

### DAO Improvements

Extended ProductDAO with:

- create(Product)
- existsByName(String)

Improved DAO exception handling by throwing custom DatabaseExceptions instead of silently returning default values.

---

### Logging Improvements

Improved structured logging throughout:

- Product creation
- Product retrieval
- Update workflow
- Generated key retrieval
- SQL execution status

---

## Learned

### DTO Separation

Learned why enterprise applications separate:

- Request DTOs
- Response DTOs
- Database Entities

instead of exposing database models directly to clients.

Benefits include:

- Better API evolution
- Stronger encapsulation
- Safer validation
- Clear separation of concerns

---

### Dirty Checking

Implemented a lightweight version of manual dirty checking using UpdateUtil.

Only changed fields are copied into the existing entity before persistence.

This mirrors the concept of Hibernate's dirty checking.

---

### Generated Keys

Learned how JDBC retrieves generated IDs using:

```
Statement.RETURN_GENERATED_KEYS
```

after INSERT statements.

---

### Service Layer Responsibility

Strengthened the role of the Service layer by making it responsible for:

- validation
- entity retrieval
- update comparison
- mapping
- persistence orchestration

instead of placing business logic inside DAOs.

---

## Problems Encountered

### 1) Partial UPDATE SQL

#### Problem

Wanted the DAO to update only modified database columns instead of executing a full UPDATE statement.

#### Investigation

Hibernate supports this through dirty checking and optional dynamic updates.

In plain JDBC this requires manually building SQL statements dynamically.

#### Decision

For BakeCheri v2, keep UPDATE statements static for simplicity and maintainability.

Perform change detection inside the Service layer using UpdateUtil before executing the UPDATE.

Dynamic SQL generation may be explored separately as a learning exercise.

---

### 2) Validation Design

#### Problem

Validation utilities were difficult to design before POST and PUT endpoints were fully implemented.

#### Resolution

Started with reusable low-level validation methods (required fields, blank checks) and planned to expand validators alongside endpoint development instead of prematurely implementing every validation rule.

---

## Next

- Finish ProductDAO.update()
- Implement ProductServlet POST endpoint
- Add request body parsing using Jackson
- Implement product creation validation
- Add duplicate product name validation
- Implement PUT endpoint
- Add centralized exception handling strategy

---
---

# Development Log

## <center> DAY FOUR </center>

## Completed

### Product Validation
- Implemented `ValidationUtils`
  - Object validation
  - String validation
    - requireNotBlank()
    - requireMinLength()
    - requireMaxLength()
    - requireLengthBetween()
    - requirePattern()
  - Number validation
    - requirePositive()
    - requireNonNegative()
    - requireBetween()
  - Collection validation
    - requireNotEmpty()
  - Helper methods
    - hasValue()

- Implemented `ProductValidator`
  - Validation for `CreateProductRequest`
  - Validation for `UpdateProductRequest`
  - Field-specific validation
    - Product Name
    - Description
    - Category
    - Price
    - Stock Quantity
    - Image URL

---

### Product Creation (POST)

Implemented complete Create Product flow.

Request Flow:

Client
→ ProductServlet
→ ProductService
→ ProductValidator
→ ProductMapper
→ ProductDAO
→ PostgreSQL

Implemented:

- `CreateProductRequest`
- Product validation
- Duplicate product-name checking
- Product entity mapping
- Product persistence
- Generated ID retrieval using
  `Statement.RETURN_GENERATED_KEYS`
- HTTP **201 Created** response

---

### Product Update (PUT)

Implemented complete Update Product flow.

Features:

- Retrieve existing product
- Partial updates
- Update only modified fields
- Name uniqueness validation
- DTO mapping
- Persist updated entity
- Return updated product response

Created:

- `UpdateProductRequest`
- `UpdateUtil`

`UpdateUtil.updateIfChanged(...)`

Supports:

- Generic types
- String-specific updates
- Cleaner update logic
- Eliminates repetitive null/equality checks

---

### Product Delete (DELETE)

Implemented Delete endpoint.

Flow:

Client
→ Servlet
→ Service
→ DAO
→ PostgreSQL

Features:

- Validate ID parameter
- Handle invalid IDs
- Delete existing product
- Return ResourceNotFoundException when ID does not exist
- Return success response when deletion succeeds

---

### DAO Improvements

Added helper queries:

- existsByName()
- existsByNameExcludingId()

Refactored duplicate query logic into:

- private exists(PreparedStatement)

to eliminate duplicated ResultSet handling.

Improved SQL:

- Used `SELECT EXISTS (...)`
  instead of counting rows.

---

### Service Layer Improvements

Added business validation:

- Product name uniqueness
- Product existence checking
- Separation of validation and persistence responsibilities

Private helper methods:

- validateProductNameIsUnique()
- getSingleProduct()

---

### Servlet Improvements

Added support for:

- POST
- PUT
- DELETE

Improved request handling:

- JSON parsing
- Invalid JSON handling
- Request validation
- Proper HTTP status codes
- Consistent API responses

---

### API Response

Integrated generic ApiResponse across all Product endpoints.

Successful responses now return:

- success
- data
- message

Error responses follow the same structure.

---

## Learned

### Request DTOs vs Response DTOs

Separate request models from response models.

Examples:

- CreateProductRequest
- UpdateProductRequest
- ProductResponse

This prevents:

- exposing internal entity structure
- accidental client-controlled fields
- unnecessary serialization

---

### Validation belongs in the Service Layer

Servlets should only:

- read requests
- send responses

Business validation belongs inside services.

---

### Partial Updates

PATCH-like behavior can be implemented in PUT by comparing incoming values against the existing entity before updating.

A reusable helper (`UpdateUtil`) significantly reduces repetitive code.

---

### Using SELECT EXISTS

Instead of:

SELECT COUNT(*)

Use:

SELECT EXISTS(...)

Benefits:

- Stops scanning once a row is found
- More expressive
- Better performance for existence checks

---

### RETURN_GENERATED_KEYS

Learned how JDBC retrieves database-generated IDs after INSERT statements using:

Statement.RETURN_GENERATED_KEYS

instead of performing an additional SELECT query.

---

## Problems Encountered

### 1) Designing Validation Before POST Endpoint

Initially attempted to build validation utilities before implementing Create Product.

#### Cause

It was difficult to anticipate which validation methods were actually needed without a real request model.

#### Resolution

Implemented POST first, then allowed the request model to naturally drive the validation utilities.

#### Learned

Validation utilities should evolve from real business requirements rather than trying to predict every future validation rule.

---

### 2) Update Only Modified Fields

Wanted behavior similar to Hibernate's dirty checking.

#### Cause

JDBC updates every column manually.

#### Resolution

Created `UpdateUtil` to compare existing values against incoming values before applying updates.

#### Learned

While JDBC lacks automatic dirty checking, reusable helper utilities can produce a similar developer experience while keeping update logic clean.

---

## Next

- Implement Global Exception Handling
- Centralize error responses
- Refactor ProductServlet to remove repetitive try/catch blocks
- Add Filter and Listener implementations
- Improve API documentation
- Begin User module (Authentication)