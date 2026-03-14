# Technology Stack

## 1. Purpose

Tài liệu này mô tả toàn bộ công nghệ được sử dụng trong hệ thống **Tech Blog Java – Tech Review Platform**.

Mục tiêu của tài liệu:

- Chuẩn hóa công nghệ sử dụng trong dự án
- Tránh việc các thành viên sử dụng framework khác nhau cho cùng một mục đích
- Giúp thành viên mới hiểu nhanh kiến trúc kỹ thuật của hệ thống
- Làm cơ sở cho việc mở rộng và bảo trì hệ thống trong tương lai

---

# 2. System Overview

Hệ thống được xây dựng theo mô hình **RESTful Backend API**.

Kiến trúc tổng thể:

Client (Frontend / Mobile)
↓
REST API (Spring Boot)
↓
Service Layer (Business Logic)
↓
Repository Layer (JPA / Hibernate)
↓
MySQL Database

Backend chịu trách nhiệm:

- Authentication & Authorization
- Business Logic
- Data Persistence
- API Service
- Content Moderation
- AI Sentiment Processing

---

# 3. Backend Core Technology

Backend được phát triển bằng **Java và Spring Boot**.

| Technology | Purpose |
|------------|--------|
| Java 17 | Ngôn ngữ lập trình chính |
| Spring Boot | Framework phát triển backend |
| Spring Web | Xây dựng REST API |
| Spring Data JPA | ORM để làm việc với database |
| Hibernate | Implementation của JPA |

Lý do lựa chọn Spring Boot:

- Ecosystem lớn
- Dễ mở rộng
- Tích hợp nhiều module sẵn
- Phù hợp cho REST API backend

---

# 4. Security

Hệ thống sử dụng **JWT-based authentication**.

| Technology | Purpose |
|------------|--------|
| Spring Security | Framework bảo mật |
| JWT (JSON Web Token) | Xác thực API |
| BCrypt | Hash mật khẩu |

Luồng xác thực:

1. User đăng nhập
2. Server kiểm tra mật khẩu
3. Server tạo JWT token
4. Client gửi token trong Authorization header
5. Server xác thực token qua JWT filter

Authorization header format:

Authorization: Bearer <token>

---

# 5. Database

Hệ thống sử dụng **MySQL** làm database chính.

| Technology | Purpose |
|------------|--------|
| MySQL 8+ | Relational database |
| InnoDB | Storage engine |
| JDBC | Database connectivity |

Database được thiết kế theo mô hình **Relational Database Model**.

Các bảng chính của hệ thống:

- users
- roles
- user_roles
- categories
- products
- product_images
- product_specs
- product_ratings
- posts
- post_moderation_logs
- reviews
- review_scores
- review_moderation_logs
- comments
- comment_reports
- comment_moderation_logs

Database được tối ưu bằng:

- Indexes
- Foreign keys
- Constraints

---

# 6. API Documentation

Hệ thống sử dụng **OpenAPI / Swagger** để document API.

| Technology | Purpose |
|------------|--------|
| Swagger | API documentation |
| OpenAPI | API specification |
| springdoc-openapi | Integration với Spring Boot |

Swagger UI cho phép:

- Xem toàn bộ API endpoint
- Test API trực tiếp
- Xem request body
- Xem response structure

Swagger URL:

/swagger-ui/index.html

---

# 7. Build Tool

Project sử dụng **Maven** làm build tool.

| Tool | Purpose |
|------|--------|
| Maven | Dependency management |
| Maven Wrapper | Đảm bảo version Maven thống nhất |

Các file quan trọng:

pom.xml  
mvnw  
mvnw.cmd

Maven quản lý:

- Dependencies
- Build lifecycle
- Packaging
- Plugin configuration

---

# 8. Project Structure Style

Hệ thống sử dụng **Domain-based package structure**.

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

Mỗi module bao gồm:

controller  
service  
repository  
model  
dto  
mapper

Ý nghĩa từng thành phần:

controller  
Xử lý HTTP request và response. Đây là lớp giao tiếp giữa client và backend.

service  
Chứa business logic của hệ thống. Service gọi repository để truy cập dữ liệu.

repository  
Tầng truy cập database sử dụng Spring Data JPA.

model  
Entity mapping với bảng trong database.

dto  
Data Transfer Object dùng để truyền dữ liệu giữa các layer.

mapper  
Chuyển đổi giữa Entity và DTO.

Cách tổ chức theo domain giúp:

- Module rõ ràng
- Dễ mở rộng hệ thống
- Giảm coupling giữa các module
- Dễ maintain code

---

# 9. Logging

Hệ thống sử dụng **Logback** để quản lý log.

| Technology | Purpose |
|------------|--------|
| Logback | Logging framework |
| SLF4J | Logging API |

Các loại log:

- Application log
- Error log
- Security log
- Request log

File cấu hình logging:

logback-spring.xml

Logs được lưu tại thư mục:

/logs

---

# 10. Development Tools

Các công cụ được sử dụng trong quá trình phát triển.

| Tool | Purpose |
|------|--------|
| IntelliJ IDEA | IDE chính |
| Git | Version control |
| GitHub / GitLab | Source repository |
| Postman | API testing |
| HeidiSQL | Database management |
| Docker | Container deployment |

---

# 11. Configuration Management

Hệ thống sử dụng **Spring Profiles** để quản lý môi trường.

Các profile:

| Profile | Purpose |
|--------|--------|
| dev | Development |
| prod | Production |

Các file config:

application.yml  
application-dev.yml  
application-prod.yml

Config bao gồm:

- Database
- JWT secret
- Email service
- Logging
- Server port

---

# 12. Deployment

Backend có thể deploy theo nhiều cách.

### 1. Jar Deployment

java -jar tech-blog-java.jar

### 2. Docker Deployment

Ứng dụng có thể được container hóa bằng Docker.

### 3. Cloud Deployment

Có thể deploy trên:

- VPS
- AWS
- Google Cloud
- Azure

---

# 13. Future Technologies (Optional)

Các công nghệ có thể bổ sung trong tương lai:

| Technology | Purpose |
|------------|--------|
| Redis | Cache |
| Elasticsearch | Full text search |
| Kafka | Event streaming |
| AI service | Sentiment analysis |
| Object Storage | Lưu ảnh sản phẩm |

---

# 14. Technology Principles

Nguyên tắc sử dụng công nghệ trong dự án:

1. Không thêm framework mới nếu chưa thảo luận với team.
2. Ưu tiên sử dụng thư viện đã có trong project.
3. Mọi dependency mới phải được review trước khi merge.
4. Tuân thủ kiến trúc backend đã thiết kế.
5. Tuân thủ coding conventions của project.

---

# 15. Summary

Tech Blog Java backend được xây dựng với stack:

Core Stack

- Java 17
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- MySQL

Supporting Tools

- Swagger
- Maven
- Logback
- Docker

Stack này đảm bảo:

- Dễ phát triển
- Dễ bảo trì
- Dễ mở rộng
- Phù hợp cho hệ thống backend REST API.