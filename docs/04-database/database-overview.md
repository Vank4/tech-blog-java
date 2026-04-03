# Database Overview

## 1. Purpose

Tài liệu này mô tả tổng quan về hệ thống database của **Tech Blog Java Backend**.

Mục tiêu:

- Giải thích cấu trúc dữ liệu của hệ thống
- Mô tả các nhóm bảng chính
- Làm cơ sở cho thiết kế ERD và table definitions
- Giúp developer hiểu cách dữ liệu được tổ chức

Hệ thống sử dụng **MySQL** làm database chính.

---

# 2. Database Technology

Database được xây dựng trên:

| Technology | Description |
|------------|-------------|
| MySQL 8+ | Relational Database |
| InnoDB | Storage Engine |
| UTF8MB4 | Character Encoding |

Lý do sử dụng MySQL:

- phổ biến
- ổn định
- dễ triển khai
- tích hợp tốt với Spring Data JPA

---

# 3. Database Architecture

Database được thiết kế theo mô hình **Relational Database Model**.

Các đặc điểm:

- sử dụng Primary Key
- sử dụng Foreign Key
- đảm bảo referential integrity
- chuẩn hóa dữ liệu

Quan hệ giữa các bảng bao gồm:

- One-to-One
- One-to-Many
- Many-to-Many

---

# 4. Database Modules

Database được chia thành các nhóm bảng theo module nghiệp vụ.

---

## 4.1 User Management

Quản lý người dùng và phân quyền.

Tables:

- users
- roles
- user_roles

Mục đích:

- lưu thông tin user
- phân quyền role
- quản lý trạng thái user

---

## 4.2 Category System

Quản lý phân loại nội dung.

Tables:

- categories

Chức năng:

- phân loại sản phẩm
- phân loại bài viết
- hỗ trợ category cha/con

---

## 4.3 Product Management

Quản lý thông tin sản phẩm.

Tables:

- products
- product_images
- product_specs

Chức năng:

- lưu thông tin sản phẩm
- lưu hình ảnh sản phẩm
- lưu thông số kỹ thuật

---

## 4.4 Rating System

Lưu điểm đánh giá sản phẩm từ người dùng.

Tables:

- product_ratings

Chức năng:

- user rating
- tính điểm trung bình

---

## 4.5 Post System

Quản lý bài viết blog.

Tables:

- posts
- post_moderation_logs

Chức năng:

- lưu bài viết
- kiểm duyệt bài viết

---

## 4.6 Review System

Quản lý bài review sản phẩm.

Tables:

- reviews
- review_scores
- review_moderation_logs

Chức năng:

- review sản phẩm
- lưu điểm chi tiết
- kiểm duyệt review

---

## 4.7 Comment System

Quản lý comment của người dùng.

Tables:

- comments
- comment_reports
- comment_moderation_logs

Chức năng:

- comment bài viết
- reply comment
- report comment
- kiểm duyệt comment

---

## 4.8 AI Analysis

Lưu kết quả phân tích AI.

Tables:

- sentiment analysis tables

Chức năng:

- phân tích cảm xúc comment
- lưu sentiment label

---

## 4.9 Analytics

Lưu dữ liệu thống kê.

Tables:

- analytics tables

Chức năng:

- thống kê hệ thống
- dashboard admin

---

# 5. Main Entities

Các entity quan trọng trong database:

| Entity | Description |
|------|-------------|
| User | người dùng hệ thống |
| Role | quyền của user |
| Product | sản phẩm công nghệ |
| Category | phân loại |
| Post | bài viết blog |
| Review | bài review sản phẩm |
| Comment | bình luận |
| Rating | điểm đánh giá |

---

# 6. Relationships

Các quan hệ chính:

User → Review (One-to-Many)

User → Comment (One-to-Many)

Product → Review (One-to-Many)

Product → Rating (One-to-Many)

Post → Comment (One-to-Many)

Category → Product (One-to-Many)

Category → Post (One-to-Many)

User ↔ Role (Many-to-Many)

---

# 7. Naming Conventions

Các quy tắc đặt tên trong database:

### Table Name

- sử dụng **snake_case**
- dùng **plural form**

Ví dụ:

```
users
products
product_images
post_moderation_logs
```

---

### Column Name

- sử dụng snake_case

Ví dụ:

```
created_at
updated_at
product_id
user_id
```

---

### Primary Key

Tên chuẩn:

```
id
```

---

### Foreign Key

Quy tắc:

```
<entity>_id
```

Ví dụ:

```
user_id
product_id
post_id
```

---

# 8. Audit Fields

Hầu hết các bảng đều có các trường audit:

```
created_at
updated_at
created_by
updated_by
```

Các trường này được quản lý bởi:

```
BaseAuditEntity
```

---

# 9. Data Integrity

Database đảm bảo integrity bằng:

- Primary Key
- Foreign Key
- Unique Constraints
- Indexes

Ví dụ:

- email phải unique
- mỗi user chỉ rating một sản phẩm một lần

---

# 10. Database Indexing

Các bảng quan trọng được index:

- users.email
- products.category_id
- comments.post_id
- reviews.product_id

Index giúp:

- tăng tốc truy vấn
- cải thiện hiệu năng

---

# 11. Migration Strategy

Database schema được quản lý qua:

- migration scripts
- version control

Schema thay đổi phải:

- update migration
- review trước khi deploy

---

# 12. Backup Strategy

Database cần được backup định kỳ.

Chiến lược backup:

- daily backup
- weekly full backup

---

# 13. Summary

Database Tech Blog Java được thiết kế theo:

- Relational model
- MySQL
- Module-based structure

Các module dữ liệu chính:

- User Management
- Product Management
- Post System
- Review System
- Comment System
- AI Analysis

Thiết kế này giúp:

- dữ liệu rõ ràng
- dễ mở rộng
- dễ maintain
- hiệu năng tốt cho hệ thống backend.