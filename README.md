# <center>Introduction</center>
>This project is dedicated to migrate my original MERN backend project:<a href="https://bakecheri-client.vercel.app/"> BakeCheri </a> to pure Java Servlets, document every development days, implement production-inspired features, and later evolved the same project into Spring Boot and AWS.

## <center>Tech Stack</center>
### Frontend:
- ReactJS (Deployed in Vercel)
### Backend:
- Java 17
- Jakarta Servlet API
- Apache Tomcat
- PostgreSQL
- JDBC
- HikariCP
- Jackson Databind
- SLF4J

## <center>Features</center>
## Implemented

### Product API
- ✅ Get all products
- ✅ Get product by ID
- ✅ Search products
- ✅ Filter by category
- ✅ Get popular products
- ✅ Create product
- ✅ Update product
- ✅ Delete product

### Backend Infrastructure
- DTO mapping
- Layered architecture
- JDBC + HikariCP
- PostgreSQL
- Jackson JSON serialization
- Generic API responses
- Validation utilities
- Custom exception hierarchy
- Reusable update utilities

## <center>API Endpoints</center>
| Method  | Endpoint                  | Description               |
|---------|---------------------------|---------------------------|
| GET     | 	/products                | 	Get all products         |
| GET     | 	/products?id=1           | 	Get product by ID        |
| GET     | 	/products?popular=true   | 	Get popular products     |
| GET     | 	/products?category=Cakes | 	Get products by category |
| GET     | 	/products?search=ube     | 	Search products          |
| POST    | 	/products                | 	Create product           |
| PUT     | 	/products                | 	Update product           |
| DELETE	 | /products?id=1            | 	Delete product           |


## <center>Project Structure</center>
src 

├── config

├── dao

├── dto

├── entity

├── exception


├── mapper

├── service

├── servlet

├── util

└── validation

## <center>Progress</center>

### Phase 1 — Backend Foundation

- [x] Project setup
- [x] Tomcat deployment
- [x] Database configuration
- [x] HikariCP connection pool
- [x] Product CRUD
- [x] DTO mapping
- [x] Validation framework
- [x] Generic API response
- [x] Custom exceptions

### Phase 2 — Servlet Infrastructure

- [ ] Exception Handling Filter
- [ ] Logging Filter
- [ ] ServletContextListener
- [ ] CORS Filter

### Phase 3 — Authentication

- [ ] User module
- [ ] Login
- [ ] Register
- [ ] JWT Authentication

### Phase 4 — Order Management

- [ ] Cart
- [ ] Orders
- [ ] Order Items


## <center>Architecture (link)</center>

Documentation (links)

Roadmap (link)

Progress

How to Run