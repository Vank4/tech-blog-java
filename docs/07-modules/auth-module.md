# Auth Module

## 1. Purpose

Auth Module chịu trách nhiệm cho toàn bộ chức năng **xác thực và khởi tạo phiên truy cập API** trong hệ thống Tech Blog Java.

Module này là nền tảng bảo mật của toàn hệ thống và phải được triển khai trước các module nghiệp vụ khác.

---

# 2. Responsibilities

Auth Module chịu trách nhiệm:

- đăng ký tài khoản
- đăng nhập
- hash mật khẩu
- cấp JWT token
- verify email
- quên mật khẩu
- reset mật khẩu
- logout logic ở mức application nếu cần

---

# 3. Main Features

## 3.1 Register

Người dùng tạo tài khoản mới bằng email và mật khẩu.

Yêu cầu:

- email duy nhất
- mật khẩu được hash trước khi lưu
- tạo user với trạng thái phù hợp
- gửi email verify nếu hệ thống bật chức năng này

## 3.2 Login

Người dùng đăng nhập bằng email và mật khẩu.

Kết quả:

- kiểm tra thông tin đăng nhập
- tạo JWT token
- trả về token và thông tin user cơ bản

## 3.3 Verify Email

Xác thực email qua token.

## 3.4 Forgot Password

Tạo reset token và gửi email reset password.

## 3.5 Reset Password

Đổi mật khẩu mới thông qua token reset hợp lệ.

---

# 4. Main Packages

```text
domain/auth
├── controller
├── dto
├── model
└── service
```

---

# 5. Related Components

Ngoài `domain/auth`, module này còn liên kết mạnh với:

```text
security/
common/exception/
common/response/
infrastructure/email/
domain/user/
```

---

# 6. Main APIs

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/auth/verify-email`
- `POST /api/v1/auth/forgot-password`
- `POST /api/v1/auth/reset-password`
- `POST /api/v1/auth/logout`

---

# 7. Main Data Involved

Các bảng liên quan:

- users
- roles
- user_roles

Nếu triển khai thêm token tables thì có thể có:

- email_verification_tokens
- password_reset_tokens

---

# 8. Business Rules

- email phải unique
- password luôn được hash bằng BCrypt
- user bị ban không được login
- user chưa verify email có thể bị chặn login tùy policy
- reset token phải có expiration time
- verify token chỉ dùng được trong thời gian hợp lệ

---

# 9. Security Requirements

- không lưu password dạng plain text
- không log password
- token JWT phải có thời hạn
- secret JWT không được hardcode trong source public
- mọi lỗi auth trả về đúng chuẩn 401 hoặc 403

---

# 10. Dependencies

Auth Module phụ thuộc vào:

- User Module để lưu user
- Security package để tạo và validate JWT
- Email infrastructure để gửi verify/reset mail

Auth Module là dependency của:

- User Module
- Product Module
- Post Module
- Review Module
- Comment Module
- Admin Module

---

# 11. Suggested Service Methods

Ví dụ các method chính:

- `register(RegisterRequest request)`
- `login(LoginRequest request)`
- `verifyEmail(String token)`
- `forgotPassword(ForgotPasswordRequest request)`
- `resetPassword(ResetPasswordRequest request)`
- `logout()`

---

# 12. Validation Rules

- email đúng định dạng
- password tối thiểu 6 ký tự
- fullName không được để trống
- token không được null hoặc rỗng

---

# 13. Error Cases

- email đã tồn tại
- email không tồn tại
- mật khẩu sai
- token không hợp lệ
- token hết hạn
- user bị khóa
- user chưa xác thực email

---

# 14. Development Notes

- nên hoàn thành module này sớm nhất
- login flow phải test kỹ
- nên có account admin seed sẵn để test các module khác
- cần đồng bộ chặt với SecurityConfig và JwtAuthenticationFilter

---

# 15. Definition of Done

Auth Module được xem là hoàn thành khi:

- đăng ký hoạt động
- đăng nhập trả JWT đúng
- protected API nhận được user từ token
- verify email hoạt động nếu bật
- reset password hoạt động
- swagger test được các API chính
- response và exception đúng chuẩn