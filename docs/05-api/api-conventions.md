# API Conventions

## 1. Purpose

Tài liệu này định nghĩa các quy tắc chung cho tất cả REST API trong hệ thống **Tech Blog Java Backend**.

Mục tiêu:

- Chuẩn hóa thiết kế API
- Đảm bảo consistency
- Giúp frontend dễ sử dụng
- Giúp developer implement đúng chuẩn

---

# 2. Base URL

Tất cả API sử dụng prefix:

```
/api/v1
```

Ví dụ:

```
/api/v1/auth/login
/api/v1/products
/api/v1/comments
```

---

# 3. HTTP Methods

| Method | Usage |
|------|------|
| GET | Lấy dữ liệu |
| POST | Tạo dữ liệu |
| PUT | Cập nhật toàn bộ |
| PATCH | Cập nhật một phần |
| DELETE | Xóa dữ liệu |

---

# 4. Naming Convention

API endpoint sử dụng:

- lowercase
- kebab-case
- plural nouns

Ví dụ:

```
/products
/product-images
/product-specs
```

---

# 5. Response Format

Tất cả API sử dụng format response chuẩn:

```
{
  "success": true,
  "message": "Request successful",
  "data": {}
}
```

---

# 6. Pagination

Các API list phải hỗ trợ pagination.

Query parameters:

```
?page=0
&size=10
&sort=createdAt,desc
```

Ví dụ:

```
GET /api/v1/products?page=0&size=20
```

Response:

```
{
  "success": true,
  "message": "Products retrieved",
  "data": {
    "content": [],
    "page": 0,
    "size": 20,
    "totalElements": 120
  }
}
```

---

# 7. Filtering

API có thể hỗ trợ filter bằng query params.

Ví dụ:

```
GET /api/v1/products?category=smartphones
GET /api/v1/posts?status=PUBLISHED
```

---

# 8. Authentication

Protected API yêu cầu JWT token.

Header:

```
Authorization: Bearer <token>
```

---

# 9. Status Codes

| Code | Meaning |
|----|----|
| 200 | Success |
| 201 | Created |
| 400 | Bad request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not found |
| 500 | Server error |

---

# 10. Error Response

```
{
  "success": false,
  "message": "Error message",
  "data": null
}
```

---

# 11. Versioning

API sử dụng version trong URL:

```
/api/v1
```

Trong tương lai có thể:

```
/api/v2
```

---

# 12. Swagger Documentation

Tất cả API được document tại:

```
/swagger-ui/index.html
```

Swagger hiển thị:

- request body
- response
- endpoint list
- test API

---

# 13. Security Rules

- API admin phải yêu cầu role ADMIN
- API user yêu cầu authentication
- public API không cần login

Ví dụ:

Public:

```
GET /products
GET /posts
```

Protected:

```
POST /comments
POST /reviews
```

Admin:

```
/api/admin/users
/api/admin/moderation
```

---

# 14. Best Practices

Các nguyên tắc thiết kế API:

1. API phải stateless
2. Không expose entity trực tiếp
3. Sử dụng DTO cho request và response
4. Luôn validate input
5. Luôn dùng ApiResponse wrapper