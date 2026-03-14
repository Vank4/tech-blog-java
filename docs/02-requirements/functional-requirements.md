# Functional Requirements

Tài liệu này mô tả các chức năng mà hệ thống Tech Blog Java phải cung cấp.

---

# 1. Authentication

## 1.1 User Registration
Người dùng có thể đăng ký tài khoản bằng email.

Yêu cầu:

- Email phải duy nhất
- Mật khẩu được mã hóa bằng BCrypt
- Hệ thống gửi email xác thực
- Tài khoản chưa xác thực không thể đăng nhập

---

## 1.2 Login

Người dùng đăng nhập bằng email và mật khẩu.

Kết quả:

- Hệ thống trả về JWT token
- Token được dùng để xác thực các request sau

---

## 1.3 Password Reset

Người dùng có thể reset mật khẩu.

Luồng:

1. User nhập email
2. Hệ thống tạo reset token
3. Gửi email reset
4. User nhập mật khẩu mới

---

# 2. User Management

## 2.1 Profile Management

User có thể:

- Xem thông tin cá nhân
- Cập nhật thông tin
- Đổi mật khẩu

---

## 2.2 Admin User Management

Admin có thể:

- Xem danh sách user
- Tìm kiếm user
- Ban / unban user
- Gán role

---

# 3. Product Management

## 3.1 Product CRUD

Admin có thể:

- Tạo sản phẩm
- Cập nhật sản phẩm
- Ẩn sản phẩm
- Xóa sản phẩm

---

## 3.2 Product Information

Thông tin sản phẩm bao gồm:

- Tên
- Mô tả
- Giá
- Hình ảnh
- Thông số kỹ thuật

---

## 3.3 Product Listing

User có thể:

- Xem danh sách sản phẩm
- Lọc theo category
- Lọc theo brand
- Sắp xếp theo rating

---

# 4. Rating System

User có thể chấm điểm sản phẩm từ 1 đến 5 sao.

Yêu cầu:

- Một user chỉ được rating một sản phẩm một lần
- Rating có thể cập nhật
- Hệ thống tính rating trung bình

---

# 5. Category System

Admin có thể:

- Tạo category
- Sửa category
- Disable category

Category có thể có cấu trúc cha/con.

---

# 6. Post Management

## 6.1 Create Post

Author có thể tạo bài viết.

Trạng thái bài viết:

- DRAFT
- PENDING
- PUBLISHED
- REJECTED

---

## 6.2 Post Moderation

Luồng:

1. Author submit bài viết
2. Admin duyệt
3. Bài được publish hoặc reject

---

# 7. Review System

Author có thể viết review cho sản phẩm.

Review gồm:

- Pros
- Cons
- Overall score
- Criteria scores

Review phải được admin duyệt trước khi hiển thị.

---

# 8. Comment System

User có thể:

- Comment
- Reply comment

Comment được tổ chức theo dạng cây.

---

# 9. Comment Report

User có thể report comment.

Admin có thể:

- Xem report
- Hide comment
- Delete comment

---

# 10. AI Sentiment Analysis

Khi có comment mới:

1. Hệ thống gửi nội dung tới AI service
2. AI trả về sentiment

Các nhãn sentiment:

- Positive
- Neutral
- Negative

---

# 11. Analytics

Admin dashboard hiển thị:

- Số lượng user
- Số lượng bài viết
- Số lượng comment
- Thống kê sentiment