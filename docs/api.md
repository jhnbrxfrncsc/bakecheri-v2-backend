# API Documentation

## Base URL

```
http://localhost:8080/BakeCheri_v2
```

---

# Products

---

## Get All Products

### Request

```
GET /products
```

---

## Get Product by ID

### Request

```
GET /products?id=1
```

---

## Get Popular Products

### Request

```
GET /products?popular=true
```

---

## Get Products by Category

### Request

```
GET /products?category=Cake
```

---

## Search Products

### Request

```
GET /products?search=Chocolate
```

Searches:

- name
- description
- category

---

## Create Product

### Request

```
POST /products
```

### Request Body

```json
{
  "name": "Chocolate Cake",
  "description": "Premium chocolate cake",
  "category": "Cake",
  "price": 499,
  "stockQuantity": 20,
  "imageUrl": "https://sample.com/cake.png"
}
```

---

## Update Product

### Request

```
PUT /products
```

### Request Body

```json
{
  "id": 1,
  "price": 599,
  "popular": true
}
```

Notes:

- `id` is required.
- All other fields are optional.
- Only modified fields are updated.

---

## Delete Product

### Request

```
DELETE /products?id=1
```

---

# Health Check

```
GET /health
```

---

# Standard Response

## Success

```json
{
    "success": true,
    "data": {},
    "message": "..."
}
```

## Error

```json
{
    "success": false,
    "data": null,
    "message": "..."
}
```

---

# HTTP Status Codes

| Status | Meaning |
|---------|----------|
| 200 | OK |
| 201 | Created |
| 400 | Bad Request |
| 404 | Not Found |
| 409 | Conflict |
| 500 | Internal Server Error |