# Backend Architecture

## 1. Purpose

Tài liệu này mô tả kiến trúc backend của hệ thống **Tech Blog Java**.

Mục tiêu của tài liệu:

- Giải thích cách backend được tổ chức
- Mô tả luồng xử lý request trong hệ thống
- Chuẩn hóa cách các module backend hoạt động
- Giúp các thành viên trong team hiểu rõ kiến trúc hệ thống

Backend được xây dựng theo mô hình **Layered Architecture kết hợp Domain-based Package Structure**.

---

# 2. High Level Architecture

Kiến trúc backend tổng thể:

Client (Web / Mobile)  
↓  
Controller Layer  
↓  
Service Layer  
↓  
Repository Layer  
↓  
Database (MySQL)

Ngoài ra hệ thống còn có:

- Security Layer
- Infrastructure Layer
- Common Utilities

---

# 3. Main Layers

Backend được chia thành các tầng chính sau.

---

## 3.1 Controller Layer

Controller là lớp xử lý **HTTP request từ client**.

Nhiệm vụ:

- Nhận request từ client
- Validate dữ liệu đầu vào
- Gọi service để xử lý logic
- Trả response về client

Ví dụ:

```
POST /api/auth/login
GET /api/products
POST /api/comments
```

Controller **không chứa business logic**.

Chỉ nên:

- validate
- mapping request
- gọi service

---

## 3.2 Service Layer

Service chứa **business logic của hệ thống**.

Nhiệm vụ:

- Xử lý nghiệp vụ
- Gọi repository để truy cập dữ liệu
- Xử lý transaction
- Điều phối nhiều repository

Ví dụ:

ProductService

- createProduct
- updateProduct
- getProductDetail
- deleteProduct

---

## 3.3 Repository Layer

Repository là tầng **truy cập database**.

Sử dụng:

Spring Data JPA

Repository chịu trách nhiệm:

- query dữ liệu
- CRUD operations
- pagination
- custom query

Ví dụ:

```
ProductRepository
UserRepository
CommentRepository
ReviewRepository
```

---

## 3.4 Model Layer

Model là **Entity mapping với database table**.

Sử dụng annotation JPA:

```
@Entity
@Table
@Id
@ManyToOne
@OneToMany
```

Ví dụ:

```
User
Product
Post
Comment
Review
Category
```

---

## 3.5 DTO Layer

DTO (Data Transfer Object) dùng để:

- truyền dữ liệu giữa client và server
- tránh expose entity trực tiếp

Các loại DTO:

Request DTO  
Response DTO

Ví dụ:

```
CreateProductRequest
ProductResponse
LoginRequest
UserResponse
```

---

# 4. Package Architecture

Backend sử dụng **Domain-based package structure**.

```
com.techblog
├── common
├── config
├── domain
│   ├── auth
│   ├── user
│   ├── category
│   ├── product
│   ├── rating
│   ├── post
│   ├── review
│   ├── comment
│   ├── ai
│   └── analytics
├── infrastructure
└── security
```

Mỗi module domain có cấu trúc:

```
controller
service
repository
model
dto
mapper
```

Ví dụ:

```
domain/product
├── controller
├── service
├── repository
├── model
├── dto
└── mapper
```

---

# 5. Request Processing Flow

Luồng xử lý request trong backend:

Client gửi request

↓

Controller nhận request

↓

Validate request

↓

Controller gọi Service

↓

Service xử lý business logic

↓

Service gọi Repository

↓

Repository truy vấn database

↓

Database trả dữ liệu

↓

Service xử lý kết quả

↓

Controller trả response

↓

Client nhận response

---

# 6. Security Architecture

Hệ thống sử dụng **JWT Authentication**.

Luồng xác thực:

Client login

↓

Server kiểm tra username/password

↓

Server tạo JWT Token

↓

Client gửi token trong header

↓

Security Filter kiểm tra token

↓

Nếu hợp lệ → request tiếp tục

Nếu không hợp lệ → trả lỗi 401

Header format:

Authorization: Bearer <token>

Security components:

```
SecurityConfig
JwtAuthenticationFilter
JwtTokenProvider
CustomUserDetailsService
AuthEntryPoint
```

---

# 7. Exception Handling

Hệ thống sử dụng **Global Exception Handler**.

File:

```
GlobalExceptionHandler
```

Mục tiêu:

- chuẩn hóa error response
- tránh stacktrace trả về client

Response format:

```
{
  "success": false,
  "message": "Error message",
  "data": null
}
```

---

# 8. Response Standard

Tất cả API sử dụng response chuẩn.

Ví dụ:

```
{
  "success": true,
  "message": "Request successful",
  "data": {}
}
```

Class sử dụng:

```
ApiResponse<T>
```

---

# 9. AI Integration Architecture

Hệ thống có module **AI sentiment analysis**.

Luồng xử lý:

User tạo comment

↓

Comment lưu vào database

↓

Hệ thống gửi nội dung tới AI service

↓

AI phân tích sentiment

↓

Kết quả lưu vào database

Sentiment labels:

- Positive
- Neutral
- Negative

---

# 10. Logging Architecture

Hệ thống log các sự kiện sau:

- API request
- Authentication
- Exception
- AI processing

Logging framework:

Logback + SLF4J

---

# 11. Infrastructure Layer

Infrastructure chứa các thành phần tích hợp bên ngoài.

Ví dụ:

```
infrastructure
├── ai_client
├── email
└── storage
```

Các thành phần này giúp:

- gọi AI API
- gửi email
- lưu file

---

# 12. Scalability Considerations

Kiến trúc được thiết kế để dễ mở rộng.

Có thể bổ sung:

- Redis cache
- Elasticsearch search
- Kafka event streaming
- Microservices architecture

---

# 13. Architecture Principles

Nguyên tắc kiến trúc của hệ thống:

1. Controller không chứa business logic.
2. Service xử lý toàn bộ nghiệp vụ.
3. Repository chỉ truy cập dữ liệu.
4. Không expose entity trực tiếp ra API.
5. Luôn sử dụng DTO cho request và response.
6. Code phải tuân thủ domain-based architecture.

---

# 14. Summary

Backend Tech Blog Java được thiết kế theo:

- Layered Architecture
- Domain-based Module Structure
- RESTful API
- JWT Security
- JPA Data Layer

Kiến trúc này đảm bảo:

- dễ maintain
- dễ mở rộng
- dễ phát triển theo module
- phù hợp với hệ thống backend hiện đại.