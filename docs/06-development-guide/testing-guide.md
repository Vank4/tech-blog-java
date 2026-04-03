# Testing Guide

## 1. Purpose

Tài liệu này mô tả cách test backend **Tech Blog Java** trong quá trình development.

Mục tiêu:

- chuẩn hóa cách test
- giúp developer tự kiểm tra code trước khi push
- giảm bug khi tích hợp
- tăng độ ổn định của hệ thống

---

# 2. Types of Testing

Project ưu tiên các loại test sau:

- Manual API testing
- Unit testing
- Integration testing
- Basic security testing

---

# 3. Manual API Testing

Đây là mức test tối thiểu bắt buộc.

Công cụ có thể dùng:

- Swagger UI
- Postman

Những gì cần test thủ công:

- request hợp lệ
- request không hợp lệ
- response format
- error handling
- auth / permission

Ví dụ:

- login đúng / sai
- create product với data hợp lệ / không hợp lệ
- access admin API bằng user thường

---

# 4. Swagger Testing

Swagger URL:

```text
http://localhost:8080/swagger-ui/index.html
```

Khi test bằng Swagger:

- kiểm tra endpoint có hiển thị không
- nhập request body đúng
- kiểm tra response format
- kiểm tra status code
- test với token nếu là protected API

---

# 5. Unit Testing

Unit test tập trung vào:

- service logic
- mapper logic
- utility methods

Ví dụ:

- `ProductServiceTest`
- `AuthServiceTest`
- `CommentServiceTest`

Nguyên tắc:

- test business logic tách biệt
- mock repository nếu cần
- không phụ thuộc DB thật cho unit test

---

# 6. Integration Testing

Integration test dùng để kiểm tra:

- controller + service + repository hoạt động cùng nhau
- mapping request/response
- security filter
- database integration

Ví dụ:

- test login API
- test create product API
- test protected endpoint with JWT

---

# 7. Security Testing

Các test bảo mật tối thiểu:

- endpoint public có truy cập được không cần token
- endpoint protected có bị chặn khi không có token không
- token sai có bị trả 401 không
- user không đủ quyền có bị trả 403 không

---

# 8. Recommended Test Cases by Module

## Auth

- register thành công
- register trùng email
- login đúng
- login sai password
- login khi user bị ban

## User

- lấy profile hiện tại
- cập nhật profile
- đổi mật khẩu

## Product

- tạo sản phẩm
- cập nhật sản phẩm
- publish sản phẩm
- lấy danh sách sản phẩm

## Post

- tạo post
- submit post
- approve/reject post

## Review

- tạo review
- submit review
- approve review

## Comment

- tạo comment
- reply comment
- report comment
- hide comment

## AI

- analyze comment
- get sentiment result
- recalculate summary

---

# 9. Basic Testing Workflow

Mỗi developer nên test theo flow:

1. Chạy project local
2. Kiểm tra database
3. Test API bằng Swagger/Postman
4. Kiểm tra log
5. Kiểm tra lỗi validation
6. Kiểm tra auth / permission
7. Commit nếu ổn

---

# 10. Test Data

Có thể dùng seed data để test nhanh:

- admin account
- user account
- categories mẫu
- products mẫu

Khuyến nghị tạo thêm test data riêng nếu module cần nhiều case hơn.

---

# 11. Common Testing Mistakes

Không nên:

- chỉ test happy path
- quên test permission
- quên test input invalid
- quên test response format
- chỉ test bằng một tài khoản admin

---

# 12. Definition of Done for Testing

Một chức năng được xem là test tạm ổn khi:

- [ ] API chạy được
- [ ] response đúng format
- [ ] validation hoạt động
- [ ] auth / role check hoạt động
- [ ] không có lỗi obvious trong log
- [ ] docs cập nhật nếu API thay đổi

---

# 13. Summary

Testing guide của project tập trung vào:

- manual API testing
- service-level testing
- integration testing
- security testing cơ bản

Mỗi developer phải tự test tối thiểu trước khi push hoặc tạo PR.