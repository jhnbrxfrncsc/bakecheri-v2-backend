# <center>BakeCheri V2 Backend</center>

> BakeCheri V2 is a backend REST API built using **Java Servlets** as part of my backend engineering roadmap. This project recreates my original MERN backend while intentionally avoiding Spring Boot to deeply understand Java Web fundamentals such as HTTP, Servlets, Filters, Listeners, JDBC, Tomcat, and application architecture.

---

# Features

## Product Module

- CRUD Operations
- Search Products
- Filter by Category
- Retrieve Popular Products

## Request Processing

- Servlet-based REST API
- Global Exception Handling
- Generic API Response
- Request Utilities
- Validation Utilities

## Infrastructure

- HikariCP Connection Pool
- SLF4J Logging
- Servlet Filters
- Servlet Context Listener
- Health Check Endpoint

---

# Tech Stack

## Backend

- Java 17
- Jakarta Servlet API
- Apache Tomcat 10
- PostgreSQL
- JDBC
- Jackson
- HikariCP
- SLF4J + Logback
- Maven

---

# Architecture

See:

- docs/architecture.md

---

# Documentation

- docs/api.md
- docs/architecture.md
- docs/decisions.md
- docs/deployment.md
- docs/development-log.md

---

# Current Progress

## ✅ Completed

- Product Module
- CRUD
- Search
- Validation
- Global Exception Handling
- Filters
- Listener
- Health Endpoint

## 🚧 Next

- Authentication
- JWT
- Users Module
- Orders Module
- Cart Module
- Payments

---

# Running the Project

1. Clone repository

```bash
git clone https://github.com/yourusername/bakecheri-v2-backend.git
```

2.

Copy

```
application.properties.example
```

to

```
application.properties
```

3.

Configure PostgreSQL credentials.

4.

Run

```
scripts/run.bat
```

5.

Open

```
http://localhost:8080/BakeCheri_v2/health
```

---

# Learning Goal

This project prioritizes learning backend fundamentals over rapid development.

The long-term roadmap is:

```
Java Core
    ↓
Servlets
    ↓
Spring Framework
    ↓
Spring Boot
    ↓
AWS
```