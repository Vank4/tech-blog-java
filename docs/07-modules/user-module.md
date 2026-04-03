# User Module

## 1. Purpose

User Module chịu trách nhiệm quản lý **thông tin hồ sơ người dùng và thao tác quản trị user** trong hệ thống Tech Blog Java.

Đây là module nghiệp vụ gắn trực tiếp với account sau khi người dùng đã được xác thực.

---

# 2. Responsibilities

User Module chịu trách nhiệm:

- xem profile hiện tại
- cập nhật profile
- đổi mật khẩu
- lấy thông tin user
- quản lý trạng thái user
- ban / unban user
- gán role cho user

---

# 3. Main Features

## 3.1 View Profile

Người dùng xem thông tin cá nhân của mình.

## 3.2 Update Profile

Người dùng cập nhật:

- full name
- avatar
- bio

## 3.3 Change Password

Người dùng đổi mật khẩu khi đã đăng nhập.

## 3.4 Admin User Management

Admin có thể:

- xem danh sách user
- tìm kiếm user
- xem chi tiết user
- ban / unban user
- gán role

---

# 4. Main Packages

```text
domain/user
├── controller
├── dto
├── model
├── repository
└── service
```

---

# 5. Main APIs

- `GET /api/v1/users/me`
- `PUT /api/v1/users/me`
- `POST /api/v1/users/change-password`
- `GET /api/v1/admin/users`
- `GET /api/v1/admin/users/{id}`
- `PATCH /api/v1/admin/users/{id}/roles`
- `PATCH /api/v1/admin/users/{id}/ban`
- `PATCH /api/v1/admin/users/{id}/unban`

---

# 6. Main Data Involved

Các bảng liên quan:

- users
- roles
- user_roles

Có thể mở rộng thêm trong tương lai:

- notifications
- favorites
- compare_lists

---

# 7. Business Rules

- email là định danh chính để đăng nhập
- user bị ban không được truy cập protected API
- admin mới được gán role
- user không được tự gán role cho mình
- mật khẩu mới không được trùng mật khẩu cũ nếu policy yêu cầu
- chỉ chủ tài khoản mới được cập nhật profile của mình

---

# 8. Related Roles

- USER
- AUTHOR
- ADMIN

---

# 9. Dependencies

User Module phụ thuộc vào:

- Auth Module
- Security package
- Role repositories

Các module khác phụ thuộc vào User Module vì nhiều entity tham chiếu `user_id`.

---

# 10. Suggested Service Methods

- `getCurrentUserProfile()`
- `updateProfile(UpdateProfileRequest request)`
- `changePassword(ChangePasswordRequest request)`
- `getUsers(UserFilterRequest request)`
- `getUserById(Long id)`
- `assignRoles(Long userId, AssignRoleRequest request)`
- `banUser(Long userId, BanUserRequest request)`
- `unbanUser(Long userId)`

---

# 11. Validation Rules

- fullName không được rỗng
- avatarUrl nếu có phải hợp lệ
- bio có giới hạn độ dài
- role được gán phải tồn tại trong hệ thống

---

# 12. Error Cases

- user không tồn tại
- không đủ quyền truy cập
- mật khẩu hiện tại không đúng
- role không hợp lệ
- đang cố ban tài khoản không được phép thao tác

---

# 13. Development Notes

- nên dùng DTO rõ ràng cho admin view và self-profile view
- cần đồng bộ chặt với security context để lấy current user
- phần assign role cần kiểm soát tránh duplicate role entries

---

# 14. Definition of Done

User Module được xem là hoàn thành khi:

- lấy profile hiện tại hoạt động
- cập nhật profile hoạt động
- đổi mật khẩu hoạt động
- admin xem được danh sách user
- admin ban/unban user được
- admin gán role được
- response và permission hoạt động đúng