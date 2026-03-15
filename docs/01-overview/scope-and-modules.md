# Scope and Modules

## 1. System Scope

Backend chịu trách nhiệm:

- Quản lý dữ liệu
- Cung cấp REST API
- Xử lý business logic
- Phân quyền người dùng
- Quản lý nội dung
- Phân tích dữ liệu AI

Frontend sẽ sử dụng các API này để hiển thị giao diện.

---

# 2. Core System Modules

Hệ thống backend được chia thành các module chính sau:

## 2.1 Auth Module

Chức năng:

- Đăng ký
- Đăng nhập
- JWT authentication
- Xác thực email
- Reset mật khẩu

Tables liên quan:

- users
- roles
- user_roles

---

## 2.2 User Module

Chức năng:

- Xem profile
- Cập nhật profile
- Quản lý user
- Ban / Unban user

Tables liên quan:

- users
- roles
- user_roles

---

## 2.3 Product Module

Chức năng:

- CRUD sản phẩm
- Thông số kỹ thuật
- Hình ảnh sản phẩm
- Lọc và tìm kiếm sản phẩm

Tables liên quan:

- products
- product_specs
- product_images

---

## 2.4 Rating Module

Chức năng:

- Chấm sao sản phẩm
- Tính điểm trung bình
- Kiểm soát mỗi user chỉ rate một lần

Tables liên quan:

- product_ratings

---

## 2.5 Category Module

Chức năng:

- Phân loại nội dung
- Phân cấp category

Tables:

- categories

---

## 2.6 Post Module

Chức năng:

- CRUD bài viết
- Workflow duyệt bài

Status:

- DRAFT
- PENDING
- PUBLISHED
- REJECTED

Tables:

- posts
- post_moderation_logs

---

## 2.7 Review Module

Chức năng:

- Review sản phẩm
- Chấm điểm chi tiết
- Kiểm duyệt review

Tables:

- reviews
- review_scores
- review_moderation_logs

---

## 2.8 Comment Module

Chức năng:

- Comment bài viết
- Comment sản phẩm
- Reply comment
- Report comment

Tables:

- comments
- comment_reports
- comment_moderation_logs

---

## 2.9 AI Module

Chức năng:

- Phân tích sentiment comment
- Lưu kết quả AI
- Tổng hợp sentiment

Tables:

- ai analysis tables

---

## 2.10 Analytics Module

Chức năng:

- Dashboard
- Thống kê hệ thống
- Phân tích dữ liệu

---

# 3. Module Dependency

Module dependency:
