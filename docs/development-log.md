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