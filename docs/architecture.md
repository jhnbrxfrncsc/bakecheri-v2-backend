# Architecture

## Overview

BakeCheri v2 follows a layered architecture.

Client → Servlet → Service → DAO → PostgreSQL

---

## Request Flow

React

↓

Tomcat

↓

ProductServlet

↓

ProductService

↓

ProductDAO

↓

PostgreSQL

↓

JSON Response

---

## Packages

config

dao

dto

entity

filter

listener

service

servlet

util

---

## Design Decisions

Why Servlets?

Why JDBC?

Why PostgreSQL?

Why no Spring?