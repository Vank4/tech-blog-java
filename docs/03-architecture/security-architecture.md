# Security Architecture

## 1. Purpose

Tài liệu này mô tả kiến trúc bảo mật của hệ thống **Tech Blog Java Backend**.

Mục tiêu:

- Giải thích cơ chế xác thực và phân quyền
- Chuẩn hóa cách xử lý security trong hệ thống
- Giảm rủi ro bảo mật
- Đảm bảo tất cả API được bảo vệ đúng cách

Hệ thống sử dụng **Spring Security + JWT Authentication**.

---

# 2. Security Overview

Kiến trúc bảo mật tổng thể:

Client  
↓  
JWT Authentication  
↓  
Spring Security Filter Chain  
↓  
Authorization Rules  
↓  
Controller Layer

Các thành phần chính:

- Spring Security
- JWT Token
- Security Filter
- Role-based Access Control
- Password Encryption

---

# 3. Authentication Method

Hệ thống sử dụng **JWT (JSON Web Token)** để xác thực người dùng.

Authentication flow:

User login

↓

Server xác thực email + password

↓

Server tạo JWT token

↓

Client lưu token

↓

Client gửi token trong request header

↓

Server kiểm tra token

↓

Nếu hợp lệ → cho phép truy cập API

---

# 4. Login Flow

Luồng đăng nhập:

User gửi request login

```
POST /api/auth/login
```

Request body:

```
{
  "email": "user@email.com",
  "password": "password"
}
```

Server xử lý:

1. Tìm user theo email
2. Kiểm tra password bằng BCrypt
3. Tạo JWT token
4. Trả token về client

Response:

```
{
  "success": true,
  "data": {
    "token": "JWT_TOKEN"
  }
}
```

---

# 5. JWT Token Structure

JWT gồm 3 phần:

```
Header
Payload
Signature
```

Ví dụ payload:

```
{
  "userId": 1,
  "email": "user@email.com",
  "role": "USER",
  "exp": 1712345678
}
```

Thông tin lưu trong token:

- userId
- email
- role
- expiration time

---

# 6. Authorization Header

Client phải gửi token trong header:

```
Authorization: Bearer <token>
```

Ví dụ:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Server sẽ đọc header này trong **JWT Authentication Filter**.

---

# 7. Security Filter Chain

Spring Security sử dụng **Filter Chain** để kiểm tra request.

Flow:

Client request

↓

JWT Authentication Filter

↓

Token validation

↓

Load user details

↓

Set authentication context

↓

Controller xử lý request

---

# 8. Security Components

Package security:

```
security
├── AuthEntryPoint
├── CustomUserDetailsService
├── JwtAuthenticationFilter
├── JwtTokenProvider
└── SecurityConfig
```

---

## SecurityConfig

Cấu hình Spring Security.

Chức năng:

- cấu hình filter chain
- cấu hình endpoint public
- cấu hình authentication

Ví dụ public endpoints:

```
/api/auth/login
/api/auth/register
/swagger-ui/**
```

---

## JwtAuthenticationFilter

Filter chịu trách nhiệm:

- đọc JWT token
- validate token
- set authentication vào SecurityContext

---

## JwtTokenProvider

Chịu trách nhiệm:

- tạo JWT token
- parse token
- validate token

---

## CustomUserDetailsService

Load thông tin user từ database.

Spring Security sử dụng class này để xác thực user.

---

## AuthEntryPoint

Xử lý lỗi authentication.

Ví dụ:

- token invalid
- token expired
- user chưa login

Response:

```
401 Unauthorized
```

---

# 9. Password Security

Password được hash bằng **BCrypt**.

Ví dụ:

```
$2a$10$N9qo8uLOickgx2ZMRZo5e...
```

Nguyên tắc:

- Không lưu password dạng plain text
- Luôn hash password trước khi lưu
- So sánh password bằng BCrypt

---

# 10. Role Based Access Control

Hệ thống sử dụng **RBAC (Role Based Access Control)**.

Các role:

```
ADMIN
USER
AUTHOR
```

Phân quyền:

| Role | Permissions |
|-----|------------|
| ADMIN | quản lý toàn hệ thống |
| AUTHOR | viết bài review |
| USER | comment, rating |

Ví dụ:

Admin APIs:

```
/api/admin/**
```

User APIs:

```
/api/comments/**
```

---

# 11. Protected Endpoints

Các endpoint cần authentication:

```
/api/users/**
/api/products/**
/api/comments/**
/api/reviews/**
/api/posts/**
```

Public endpoints:

```
/api/auth/login
/api/auth/register
/swagger-ui/**
```

---

# 12. Security Best Practices

Các nguyên tắc bảo mật của hệ thống:

1. Không lưu password dạng plain text.
2. Luôn validate input từ client.
3. Không expose entity trực tiếp ra API.
4. Sử dụng DTO để truyền dữ liệu.
5. Không log thông tin nhạy cảm.

---

# 13. Protection Against Common Attacks

Hệ thống được thiết kế để giảm các nguy cơ:

### SQL Injection

- sử dụng JPA
- sử dụng prepared statements

---

### Cross Site Scripting (XSS)

- validate input
- sanitize dữ liệu

---

### Brute Force Attack

Có thể bổ sung:

- login attempt limit
- captcha

---

### Token Expiration

JWT có thời gian hết hạn.

Ví dụ:

```
token_expiration = 24h
```

Sau khi hết hạn user phải login lại.

---

# 14. Future Security Improvements

Các cải tiến bảo mật có thể bổ sung:

- Refresh Token
- OAuth2 Login
- API Rate Limiting
- Redis Token Blacklist
- Two Factor Authentication

---

# 15. Summary

Backend sử dụng hệ thống bảo mật:

Spring Security + JWT Authentication

Các cơ chế bảo mật chính:

- JWT Token Authentication
- Role Based Authorization
- BCrypt Password Encryption
- Security Filter Chain
- Global Authentication Handling

Kiến trúc này đảm bảo:

- API được bảo vệ
- user được xác thực
- phân quyền rõ ràng
- dễ mở rộng trong tương lai