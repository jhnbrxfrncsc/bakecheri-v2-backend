# Deployment Guide

This document explains how to build, configure, deploy, and run BakeCheri V2 locally.

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

Execute the SQL scripts in the following order:

1. `/sql/01_database.sql`
2. `/sql/schema/02_products.sql`
3. `/sql/schema/03_users.sql`
4. `/sql/schema/04_cart.sql`
5. `/sql/schema/05_cart_items.sql`
6. `/sql/06_seed.sql`

---

# Configuration

Create the following file:

```
src/main/resources/application.properties
```

Example:

```properties
db.url=jdbc:postgresql://localhost:5432/bakecheri
db.username=postgres
db.password=password
db.pool.max-size=10
db.pool.min-idle=5
```

This file is excluded from Git and should contain your local database configuration.

---

# Running the Application

## Option 1 (Recommended for Windows)

The project includes helper scripts located in:

```
scripts/
```

Available scripts:

| Script | Description |
|---------|-------------|
| `start_tomcat.bat` | Builds the project, deploys the WAR to Tomcat, and starts Tomcat normally. |
| `debug_tomcat.bat` | Builds the project, deploys the WAR, and starts Tomcat in JPDA debug mode for remote debugging from an IDE. |

These scripts automate the build, deployment, and Tomcat startup process.

---

## Option 2 (Manual)

### 1. Build the project using Maven:

```bash
mvn clean package
```
The generated WAR file will be located in:

```
target/BakeCheri_v2.war
```

### 2. Copy the generated WAR:

```
target/BakeCheri_v2.war
```

into your Tomcat deployment directory:

```
<TOMCAT_HOME>/webapps/
```
3. Start Apache Tomcat manually.

---

# Verify Deployment

Health endpoint:

```
GET /BakeCheri_v2/health
```

Expected response:

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

## Health

```
GET /health
```

---

## Products

Retrieve all products:

```
GET /products
```

Retrieve a product by ID:

```
GET /products?id=1
```

Retrieve products by category:

```
GET /products?category=Cake
```

Retrieve popular products:

```
GET /products?popular=true
```

Search products:

```
GET /products?search=Chocolate
```

Create a product:

```
POST /products
```

Update a product:

```
PUT /products
```

Delete a product:

```
DELETE /products?id=1
```

---

# API Testing

A ready-to-use Postman collection is included in:

```
postman/
```

The collection currently contains:

## Health

- GET `/health`

## Products

- POST `/products`
- GET `/products`
- PUT `/products`
- DELETE `/products`

The Product requests also include examples for:

- Retrieve by ID
- Retrieve by Category
- Retrieve Popular Products
- Search Products

This allows the API to be tested immediately after deployment without manually creating requests.

---

# Logging

Application logging is implemented using:

- SLF4J
- Logback

Configuration file:

```
src/main/resources/logback.xml
```

---

# Notes

BakeCheri V2 intentionally avoids Spring Boot.

The objective of this project is to demonstrate how a production-style Java backend can be built using:

- Java
- Servlets
- JDBC
- PostgreSQL
- Apache Tomcat

before migrating the same application to Spring Boot in the next version (BakeCheri V3).