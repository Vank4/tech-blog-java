# Auth API

## 1. Purpose

Tài liệu này mô tả các API liên quan đến **authentication** trong hệ thống **Tech Blog Java Backend**.

Các chức năng chính:

- đăng ký tài khoản
- đăng nhập
- xác thực email
- quên mật khẩu
- đặt lại mật khẩu

Base path:

```
/api/v1/auth
```

---

# 2. Register

## Endpoint

```
POST /api/v1/auth/register
```

## Description

Tạo tài khoản người dùng mới.

## Authentication

Không yêu cầu.

## Request Body

```json
{
  "email": "user@example.com",
  "password": "123456",
  "fullName": "Nguyen Van A"
}
```

## Validation Rules

- `email` bắt buộc, đúng định dạng email
- `password` bắt buộc, tối thiểu 6 ký tự
- `fullName` bắt buộc

## Success Response

```json
{
  "success": true,
  "message": "Register successful. Please verify your email.",
  "data": null
}
```

## Error Cases

- email đã tồn tại
- dữ liệu không hợp lệ

---

# 3. Login

## Endpoint

```
POST /api/v1/auth/login
```

## Description

Đăng nhập hệ thống bằng email và mật khẩu.

## Authentication

Không yêu cầu.

## Request Body

```json
{
  "email": "user@example.com",
  "password": "123456"
}
```

## Success Response

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "JWT_TOKEN",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "fullName": "Nguyen Van A",
      "roles": ["USER"]
    }
  }
}
```

## Error Cases

- email không tồn tại
- mật khẩu sai
- tài khoản bị khóa
- email chưa xác thực

---

# 4. Verify Email

## Endpoint

```
GET /api/v1/auth/verify-email?token=...
```

## Description

Xác thực email người dùng thông qua token gửi qua email.

## Authentication

Không yêu cầu.

## Request Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| token | string | yes | Token xác thực email |

## Success Response

```json
{
  "success": true,
  "message": "Email verified successfully",
  "data": null
}
```

## Error Cases

- token không hợp lệ
- token hết hạn
- user đã xác thực trước đó

---

# 5. Forgot Password

## Endpoint

```
POST /api/v1/auth/forgot-password
```

## Description

Gửi email chứa token reset password.

## Authentication

Không yêu cầu.

## Request Body

```json
{
  "email": "user@example.com"
}
```

## Success Response

```json
{
  "success": true,
  "message": "Reset password email has been sent",
  "data": null
}
```

## Error Cases

- email không tồn tại
- email không hợp lệ

---

# 6. Reset Password

## Endpoint

```
POST /api/v1/auth/reset-password
```

## Description

Đặt lại mật khẩu bằng reset token.

## Authentication

Không yêu cầu.

## Request Body

```json
{
  "token": "RESET_TOKEN",
  "newPassword": "newpassword123"
}
```

## Validation Rules

- `token` bắt buộc
- `newPassword` tối thiểu 6 ký tự

## Success Response

```json
{
  "success": true,
  "message": "Password reset successful",
  "data": null
}
```

## Error Cases

- token không hợp lệ
- token hết hạn
- mật khẩu mới không hợp lệ

---

# 7. Logout

## Endpoint

```
POST /api/v1/auth/logout
```

## Description

Logout ở phía client bằng cách xóa token.  
Nếu sau này hỗ trợ blacklist token, endpoint này có thể đánh dấu token là không hợp lệ.

## Authentication

Yêu cầu JWT.

## Success Response

```json
{
  "success": true,
  "message": "Logout successful",
  "data": null
}
```

---

# 8. Auth API Summary

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| /api/v1/auth/register | POST | Đăng ký tài khoản | No |
| /api/v1/auth/login | POST | Đăng nhập | No |
| /api/v1/auth/verify-email | GET | Xác thực email | No |
| /api/v1/auth/forgot-password | POST | Gửi email reset password | No |
| /api/v1/auth/reset-password | POST | Đặt lại mật khẩu | No |
| /api/v1/auth/logout | POST | Đăng xuất | Yes |