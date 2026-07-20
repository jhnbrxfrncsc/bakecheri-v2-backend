# Architectural Decisions

This document records important design decisions made throughout the development of BakeCheri V2.

---

# ADR-001 — Layered Architecture

## Status
Accepted

## Decision

The project follows a layered architecture:

```
Servlet
    ↓
Service
    ↓
DAO
    ↓
Database
```

DTOs are used between the Servlet and Service layers.

## Reason

Separating responsibilities makes the code easier to maintain, test, and extend.

Each layer has a single responsibility:

- Servlet → HTTP request/response
- Service → business logic
- DAO → database operations
- Mapper → entity/DTO conversion

## Consequences

Pros

- Easier maintenance
- Better separation of concerns
- Similar to Spring Boot architecture

Cons

- More classes than a simple CRUD application

---

# ADR-002 — DTO Pattern

## Status

Accepted

## Decision

The API never exposes Entity objects directly.

Instead:

```
Database
    ↓
Entity
    ↓
Mapper
    ↓
DTO
    ↓
JSON Response
```

Separate DTOs are also used for requests.

Examples:

- ProductResponse
- CreateProductRequest
- UpdateProductRequest

## Reason

Allows independent evolution of API contracts without affecting persistence models.

---

# ADR-003 — Service Layer Validation

## Status

Accepted

## Decision

Validation is performed inside the Service layer before persistence.

Validation is centralized inside:

```
ValidationUtils
ProductValidator
```

## Reason

Business rules belong in the Service layer instead of Servlets.

This also allows future reuse by Spring Boot controllers.

---

# ADR-004 — Custom Exceptions

## Status

Accepted

## Decision

Business failures throw custom exceptions instead of returning null.

Exceptions include:

- ValidationException
- BadRequestException
- ResourceNotFoundException
- DatabaseException

## Reason

Cleaner code.

Avoids deeply nested if/else blocks.

---

# ADR-005 — Global Exception Handling Filter

## Status

Accepted

## Decision

Servlets do not handle business exceptions.

A global ExceptionHandlingFilter converts exceptions into JSON responses.

Example:

```
ValidationException
        ↓
ExceptionHandlingFilter
        ↓
400 Bad Request
```

## Reason

Keeps Servlets focused only on request processing.

Provides consistent API responses.

---

# ADR-006 — Generic API Response

## Status

Accepted

## Decision

Every endpoint returns a common response structure.

Example:

```json
{
  "success": true,
  "data": {},
  "message": "Operation completed."
}
```

## Reason

Provides consistent responses across all endpoints.

---

# ADR-007 — Utility Classes

## Status

Accepted

## Decision

Common reusable logic is extracted into utility classes.

Examples:

- RequestUtils
- HttpResponseUtil
- ValidationUtils
- UpdateUtil

## Reason

Avoid duplicate code.

Encourages reuse.

---

# ADR-008 — XML Configuration

## Status

Accepted

## Decision

Servlets, filters, and listeners are configured using web.xml instead of annotations.

## Reason

Provides a clearer understanding of how the Servlet container initializes the application.

This also demonstrates knowledge of traditional Jakarta EE configuration before introducing annotation-based configuration in Spring Boot.

---

# ADR-009 — Partial Updates

## Status

Accepted

## Decision

PUT requests only update fields whose values have changed.

Implementation uses UpdateUtil.

## Reason

Avoid unnecessary setter calls.

Keeps update logic clean.

---

# ADR-010 — DAO Returns Optional

## Status

Accepted

## Decision

DAO methods retrieving a single entity return Optional<T>.

Example:

```java
Optional<Product> findById(Long id);
```

## Reason

Explicitly represents the possibility that a database record may not exist.

Avoids returning null.