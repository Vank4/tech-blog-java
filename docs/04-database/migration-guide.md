# Database Migration Guide

## 1. Purpose

Tài liệu này mô tả cách quản lý thay đổi schema database trong hệ thống **Tech Blog Java Backend**.

Mục tiêu:

- đảm bảo database schema luôn đồng bộ
- kiểm soát thay đổi cấu trúc database
- tránh lỗi khi deploy
- hỗ trợ team development

---

# 2. What is Database Migration

Database migration là quá trình:

- tạo bảng mới
- thay đổi cấu trúc bảng
- thêm cột
- xóa cột
- thay đổi index
- thay đổi constraint

Migration giúp:

- version hóa schema database
- deploy database an toàn

---

# 3. Migration Strategy

Project có thể sử dụng một trong hai cách:

### Option 1: Hibernate Auto Update (Development)

```
spring.jpa.hibernate.ddl-auto=update
```

Chỉ dùng cho:

- development
- testing

Không khuyến nghị cho production.

---

### Option 2: Migration Tool (Recommended)

Các tool phổ biến:

- Flyway
- Liquibase

Khuyến nghị:

```
Flyway
```

---

# 4. Flyway Migration Structure

Cấu trúc thư mục:

```
resources
└── db
    └── migration
```

Ví dụ:

```
V1__create_users_table.sql
V2__create_roles_table.sql
V3__create_products_table.sql
```

Quy tắc đặt tên:

```
V<version>__<description>.sql
```

Ví dụ:

```
V1__init_schema.sql
V2__add_product_tables.sql
V3__add_comment_tables.sql
```

---

# 5. Migration Workflow

Luồng migration:

Developer thay đổi schema

↓

Tạo migration file

↓

Commit vào repository

↓

Deploy server

↓

Flyway chạy migration

↓

Database được update

---

# 6. Example Migration

Ví dụ tạo bảng users.

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    status VARCHAR(50),
    created_at DATETIME,
    updated_at DATETIME
);
```

---

# 7. Adding New Column

Ví dụ thêm cột vào bảng.

```sql
ALTER TABLE users
ADD COLUMN avatar_url VARCHAR(255);
```

---

# 8. Creating Index

Ví dụ tạo index:

```sql
CREATE INDEX idx_users_email
ON users(email);
```

---

# 9. Migration Rules

Khi viết migration cần tuân theo:

1. Không chỉnh sửa migration đã chạy.
2. Luôn tạo migration mới cho thay đổi.
3. Migration phải idempotent nếu có thể.
4. Migration phải được review trước khi merge.

---

# 10. Development Environment

Trong development có thể:

- reset database
- chạy migration lại
- test migration scripts

---

# 11. Production Environment

Trong production:

- không xóa migration cũ
- không sửa migration đã deploy
- backup database trước khi migrate

---

# 12. Migration Version Table

Flyway tạo bảng:

```
flyway_schema_history
```

Bảng này lưu:

- version
- description
- migration time
- trạng thái migration

---

# 13. Rollback Strategy

Nếu migration gây lỗi:

Có thể rollback bằng:

- restore database backup
- hoặc tạo migration rollback

Ví dụ:

```
V10__rollback_feature.sql
```

---

# 14. Best Practices

Các nguyên tắc migration:

- migration nhỏ
- migration rõ ràng
- migration có mô tả
- migration được test

---

# 15. Summary

Database migration giúp:

- quản lý schema database
- deploy database an toàn
- đồng bộ schema giữa các môi trường

Khuyến nghị sử dụng:

```
Flyway Migration
```

cho production deployment.