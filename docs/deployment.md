# Deployment Guide

This document explains how to run BakeCheri V2 locally.

---

# Prerequisites

Install the following:

- Java 17+
- Maven 3.9+
- PostgreSQL
- Apache Tomcat 10+
- Git

---

# Clone Repository

```bash
git clone https://github.com/jhnbrxfrncsc/bakecheri-v2-backend.git
```

---

# Database

Create a PostgreSQL database.

Example:

```sql
CREATE DATABASE bakecheri;
```

Execute the SQL scripts located inside:

- First Script:
```
/sql/01_database.sql
```
- Second Script:
```
/sql/schema/02_products.sql
```
- Third Script:
```
/sql/schema/03_users.sql
```
- Fourth Script:
```
/sql/schema/04_cart.sql
```
- Fifth Script:
```
/sql/schema/05_cart_items.sql
```
- Sixth Script:
```
/sql/06_seed.sql
```

---

# Configuration

Create:

```
src/main/resources/application.properties
```

Example

```properties
db.url=jdbc:postgresql://localhost:5432/bakecheri
db.username=postgres
db.password=password
db.pool.max-size=10
db.pool.min-idle=5
```

This file is excluded from Git.

---

# Build

```bash
mvn clean package
```

The generated WAR file will be located in:

```
target/
```

---

# Deploy

Copy:

```
target/BakeCheri_v2.war
```

into

```
Tomcat/webapps/
```

Start Tomcat.

---

# Verify

Health endpoint:

```
GET /BakeCheri_v2/health
```

Expected response

```json
{
  "success": true,
  "data": {
    "status": "UP"
  },
  "message": "Application is running."
}
```

---

# API Base URL

```
http://localhost:8080/BakeCheri_v2
```

---

# Available Endpoints

Products

```
GET    /products
GET    /products?id=1
GET    /products?category=Cake
GET    /products?popular=true
GET    /products?search=Chocolate

POST   /products

PUT    /products

DELETE /products?id=1
```

---

# Logging

Application logs use SLF4J + Logback.

Configuration:

```
src/main/resources/logback.xml
```

---

# Notes

This project intentionally avoids Spring Boot.

The objective is to demonstrate how a production-style backend can be built using:

- Java
- Servlets
- JDBC
- PostgreSQL
- Tomcat

before migrating the same project to Spring Boot in the next version.