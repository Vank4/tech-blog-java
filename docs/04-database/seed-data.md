# Seed Data

## 1. Purpose

Seed data là dữ liệu khởi tạo ban đầu của hệ thống **Tech Blog Java Backend**.

Mục tiêu của seed data:

- giúp hệ thống chạy được ngay sau khi setup database
- tạo dữ liệu mặc định cho các module quan trọng
- hỗ trợ development và testing
- tránh lỗi khi hệ thống yêu cầu dữ liệu nền

Seed data thường được chạy **khi hệ thống khởi động lần đầu**.

---

# 2. Seed Data Strategy

Seed data được thực hiện bằng:

- Spring Boot DataSeeder
- SQL scripts
- hoặc migration tool

Trong project này sử dụng class:

```
DataSeeder
```

Class này chạy khi application start.

---

# 3. Seed Roles

Hệ thống cần các role mặc định.

## Roles

| Name | Description |
|-----|-------------|
| ADMIN | Quản trị hệ thống |
| USER | Người dùng thông thường |
| AUTHOR | Người viết bài |

Ví dụ SQL:

```sql
INSERT INTO roles (name, description)
VALUES ('ADMIN', 'System administrator'),
       ('USER', 'Normal user'),
       ('AUTHOR', 'Content author');
```

---

# 4. Seed Admin Account

Một tài khoản admin mặc định cần được tạo.

Thông tin mẫu:

| Field | Value |
|------|------|
| email | admin@techblog.com |
| password | admin123 (hashed) |
| role | ADMIN |

Ví dụ SQL:

```sql
INSERT INTO users (email, password, full_name, status, email_verified)
VALUES ('admin@techblog.com', '$2a$10$hashedpassword', 'System Admin', 'ACTIVE', true);
```

Sau đó gán role:

```sql
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);
```

---

# 5. Seed Default Categories

Category mặc định cho bài viết và sản phẩm.

Ví dụ:

| Name | Slug | Type |
|-----|-----|-----|
| Smartphones | smartphones | PRODUCT |
| Laptops | laptops | PRODUCT |
| Tablets | tablets | PRODUCT |
| News | news | POST |
| Reviews | reviews | POST |
| Guides | guides | POST |

Ví dụ SQL:

```sql
INSERT INTO categories (name, slug, type, enabled)
VALUES
('Smartphones', 'smartphones', 'PRODUCT', true),
('Laptops', 'laptops', 'PRODUCT', true),
('Tablets', 'tablets', 'PRODUCT', true),
('News', 'news', 'POST', true),
('Reviews', 'reviews', 'POST', true),
('Guides', 'guides', 'POST', true);
```

---

# 6. Seed Sample Products (Optional)

Trong môi trường development có thể seed sản phẩm mẫu.

Ví dụ:

| Product | Category |
|--------|---------|
| iPhone 15 | Smartphones |
| Samsung Galaxy S24 | Smartphones |
| MacBook Pro M3 | Laptops |

Ví dụ SQL:

```sql
INSERT INTO products (category_id, name, slug, brand, status)
VALUES
(1, 'iPhone 15', 'iphone-15', 'Apple', 'PUBLISHED'),
(1, 'Samsung Galaxy S24', 'samsung-galaxy-s24', 'Samsung', 'PUBLISHED'),
(2, 'MacBook Pro M3', 'macbook-pro-m3', 'Apple', 'PUBLISHED');
```

---

# 7. Seed Sample Post (Optional)

Dữ liệu mẫu cho module blog.

Ví dụ:

```sql
INSERT INTO posts (category_id, author_id, title, slug, status)
VALUES
(4, 1, 'Welcome to Tech Blog', 'welcome-tech-blog', 'PUBLISHED');
```

---

# 8. Seed Strategy Rules

Seed data phải tuân theo các nguyên tắc:

1. Không tạo dữ liệu trùng lặp.
2. Chỉ seed nếu dữ liệu chưa tồn tại.
3. Seed dữ liệu tối thiểu cần thiết.
4. Seed data không được ảnh hưởng production data.

Ví dụ logic:

```
if role ADMIN does not exist
    create ADMIN role
```

---

# 9. Development vs Production

## Development

Có thể seed:

- roles
- admin
- categories
- sample products
- sample posts

## Production

Chỉ seed:

- roles
- admin
- categories

Không nên seed:

- sample products
- sample posts

---

# 10. Seed Execution Flow

Luồng seed data khi server start:

Application start

↓

Check roles exist

↓

Insert roles if missing

↓

Check admin account

↓

Insert admin if missing

↓

Check categories

↓

Insert categories

---

# 11. DataSeeder Example

Pseudo code:

```
@Component
public class DataSeeder {

    @PostConstruct
    public void seed() {

        seedRoles();
        seedAdmin();
        seedCategories();

    }

}
```

---

# 12. Summary

Seed data đảm bảo hệ thống có dữ liệu nền cần thiết.

Seed bao gồm:

- roles
- admin account
- categories
- optional sample data

Seed data giúp:

- hệ thống chạy ngay sau khi setup
- developer test nhanh
- tránh lỗi thiếu dữ liệu.