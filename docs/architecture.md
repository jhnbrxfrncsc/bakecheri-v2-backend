# Request Flow

```
                 Client
                    │
                    ▼
             Filter Chain
                    │
    ┌───────────────┼────────────────┐
    │               │                │
    ▼               ▼                ▼
RequestLogging  CharacterEncoding  ExceptionHandling
     Filter          Filter             Filter
                    │
                    ▼
              ProductServlet
                    │
                    ▼
              RequestUtils
                    │
                    ▼
             ProductService
                    │
                    ▼
           ProductValidator
                    │
                    ▼
               ProductDAO
                    │
                    ▼
             PostgreSQL Database
```

The response then propagates back through the same chain until it reaches the client.

---

# Layer Responsibilities

## Servlet

- HTTP Request handling
- Routing
- Calls Service layer

---

## Service

- Business Logic
- Validation
- DTO ↔ Entity conversion

---

## DAO

- SQL execution
- JDBC interaction
- Result mapping

---

## Filters

- Logging
- Character Encoding
- Global Exception Handling

---

## Listener

Responsible for application startup and shutdown events.

Current responsibilities:

- Initialize database connection pool
- Shutdown HikariCP gracefully