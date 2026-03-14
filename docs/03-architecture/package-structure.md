# Package Structure

## 1. Purpose

Tài liệu này mô tả cấu trúc package của backend **Tech Blog Java**.

Mục tiêu:

- Chuẩn hóa cách tổ chức code trong project
- Giúp developer dễ tìm code
- Tránh code bị phân tán
- Dễ mở rộng hệ thống

Project sử dụng **Domain-based Package Structure** thay vì Layer-based Structure.

Điều này giúp mỗi module có đầy đủ thành phần của nó.

---

# 2. Root Package Structure

Package gốc của hệ thống:

```
com.techblog
```

Cấu trúc tổng thể:

```
com.techblog
├── common
├── config
├── domain
├── infrastructure
└── security
```

Ý nghĩa từng package:

| Package | Purpose |
|-------|--------|
| common | code dùng chung |
| config | cấu hình hệ thống |
| domain | các module nghiệp vụ |
| infrastructure | tích hợp service bên ngoài |
| security | hệ thống bảo mật |

---

# 3. Common Package

Package này chứa các thành phần dùng chung cho toàn bộ hệ thống.

```
common
├── audit
├── constants
├── enums
├── exception
├── request
├── response
├── util
└── validation
```

### audit

Chứa base entity cho audit.

Ví dụ:

```
BaseAuditEntity
```

Dùng để lưu:

- created_at
- updated_at
- created_by

---

### constants

Chứa các constant của hệ thống.

Ví dụ:

```
ApiConstants
SecurityConstants
ErrorCodes
```

---

### enums

Chứa các enum của hệ thống.

Ví dụ:

```
UserStatus
PostStatus
ProductStatus
ReviewStatus
CommentStatus
RoleName
SentimentLabel
```

---

### exception

Chứa các class xử lý lỗi.

Ví dụ:

```
GlobalExceptionHandler
BusinessException
ResourceNotFoundException
```

---

### request

Chứa các request DTO dùng chung.

---

### response

Chứa response chuẩn của hệ thống.

Ví dụ:

```
ApiResponse
```

---

### util

Các utility class.

Ví dụ:

```
DateUtils
TokenUtils
StringUtils
```

---

### validation

Custom validation annotation.

---

# 4. Config Package

Package config chứa cấu hình hệ thống.

```
config
├── AppProperties
├── CorsConfig
├── DataSeeder
└── SwaggerConfig
```

### AppProperties

Chứa cấu hình custom đọc từ `application.yml`.

---

### CorsConfig

Cấu hình CORS cho API.

---

### DataSeeder

Seed dữ liệu ban đầu:

- roles
- admin account
- default categories

---

### SwaggerConfig

Cấu hình Swagger/OpenAPI.

---

# 5. Domain Package

Đây là **package quan trọng nhất của project**.

Chứa các module nghiệp vụ.

```
domain
├── auth
├── user
├── category
├── product
├── rating
├── post
├── review
├── comment
├── interaction
├── ai
└── analytics
```

Mỗi domain module có cấu trúc giống nhau.

---

# 6. Domain Module Structure

Ví dụ module **product**:

```
domain/product
├── controller
├── service
├── repository
├── model
├── dto
└── mapper
```

Ý nghĩa:

### controller

Xử lý API request.

Ví dụ:

```
ProductController
```

---

### service

Chứa business logic.

Ví dụ:

```
ProductService
ProductServiceImpl
```

---

### repository

Truy cập database.

Ví dụ:

```
ProductRepository
```

---

### model

Entity mapping với database.

Ví dụ:

```
Product
ProductImage
ProductSpec
```

---

### dto

Data transfer objects.

Ví dụ:

```
CreateProductRequest
ProductResponse
```

---

### mapper

Chuyển đổi entity ↔ DTO.

Ví dụ:

```
ProductMapper
```

---

# 7. Security Package

Package security chứa hệ thống bảo mật.

```
security
├── AuthEntryPoint
├── CustomUserDetailsService
├── JwtAuthenticationFilter
├── JwtTokenProvider
└── SecurityConfig
```

Chức năng:

| Component | Purpose |
|----------|--------|
| SecurityConfig | cấu hình Spring Security |
| JwtAuthenticationFilter | filter kiểm tra JWT |
| JwtTokenProvider | tạo và validate token |
| CustomUserDetailsService | load user từ database |
| AuthEntryPoint | xử lý lỗi authentication |

---

# 8. Infrastructure Package

Chứa các integration với hệ thống bên ngoài.

```
infrastructure
├── ai_client
├── email
└── storage
```

Ví dụ:

### ai_client

Gọi API AI sentiment analysis.

---

### email

Gửi email:

- verify email
- reset password

---

### storage

Lưu trữ file:

- product images
- post images

---

# 9. Resources Folder

```
resources
├── static
├── templates
├── application.yml
├── application-dev.yml
├── application-prod.yml
└── logback-spring.xml
```

---

### static

CSS, JS, images.

---

### templates

HTML templates.

---

### application.yml

Cấu hình hệ thống.

---

### logback-spring.xml

Cấu hình logging.

---

# 10. Testing Structure

```
src/test/java
```

Chứa:

- unit tests
- integration tests

Ví dụ:

```
ProductServiceTest
AuthControllerTest
```

---

# 11. Package Design Principles

Các nguyên tắc thiết kế package:

1. Code được tổ chức theo **domain** thay vì layer.
2. Mỗi module phải self-contained.
3. Không import trực tiếp repository của module khác.
4. Logic phải nằm trong service layer.
5. Controller chỉ xử lý request và response.

---

# 12. Advantages

Cấu trúc package này giúp:

- Code rõ ràng
- Dễ maintain
- Dễ mở rộng module mới
- Phù hợp với Domain Driven Design
- Phù hợp với hệ thống backend lớn

---

# 13. Summary

Backend Tech Blog Java sử dụng:

Domain-based package structure

Các package chính:

```
common
config
domain
security
infrastructure
```

Mỗi domain module chứa đầy đủ:

```
controller
service
repository
model
dto
mapper
```

Thiết kế này giúp project:

- dễ phát triển
- dễ mở rộng
- dễ maintain lâu dài