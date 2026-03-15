# User API

## 1. Purpose

Tài liệu này mô tả các API liên quan đến **user management** trong hệ thống **Tech Blog Java Backend**.

Các chức năng chính:

- xem thông tin cá nhân
- cập nhật profile
- đổi mật khẩu
- xem danh sách yêu thích
- xem danh sách so sánh

Base path:

```
/api/v1/users
```

---

# 2. Get Current User Profile

## Endpoint

```
GET /api/v1/users/me
```

## Description

Lấy thông tin profile của user đang đăng nhập.

## Authentication

Yêu cầu JWT.

## Success Response

```json
{
  "success": true,
  "message": "User profile retrieved successfully",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "fullName": "Nguyen Van A",
    "avatarUrl": "https://example.com/avatar.jpg",
    "bio": "Tech enthusiast",
    "status": "ACTIVE",
    "roles": ["USER"]
  }
}
```

## Error Cases

- chưa đăng nhập
- token không hợp lệ

---

# 3. Update Profile

## Endpoint

```
PUT /api/v1/users/me
```

## Description

Cập nhật thông tin cá nhân của user.

## Authentication

Yêu cầu JWT.

## Request Body

```json
{
  "fullName": "Nguyen Van B",
  "avatarUrl": "https://example.com/new-avatar.jpg",
  "bio": "Updated bio"
}
```

## Validation Rules

- `fullName` không được để trống
- `avatarUrl` là URL hợp lệ nếu có
- `bio` không vượt quá giới hạn hệ thống

## Success Response

```json
{
  "success": true,
  "message": "Profile updated successfully",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "fullName": "Nguyen Van B",
    "avatarUrl": "https://example.com/new-avatar.jpg",
    "bio": "Updated bio"
  }
}
```

---

# 4. Change Password

## Endpoint

```
POST /api/v1/users/change-password
```

## Description

Đổi mật khẩu của user đang đăng nhập.

## Authentication

Yêu cầu JWT.

## Request Body

```json
{
  "currentPassword": "oldpassword",
  "newPassword": "newpassword123"
}
```

## Validation Rules

- `currentPassword` bắt buộc
- `newPassword` bắt buộc, tối thiểu 6 ký tự
- `newPassword` không được trùng với mật khẩu cũ

## Success Response

```json
{
  "success": true,
  "message": "Password changed successfully",
  "data": null
}
```

## Error Cases

- mật khẩu hiện tại không đúng
- mật khẩu mới không hợp lệ

---

# 5. Get Favorite List

## Endpoint

```
GET /api/v1/users/me/favorites
```

## Description

Lấy danh sách sản phẩm yêu thích của user.

## Authentication

Yêu cầu JWT.

## Success Response

```json
{
  "success": true,
  "message": "Favorite list retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "iPhone 15",
      "slug": "iphone-15"
    },
    {
      "id": 2,
      "name": "MacBook Pro M3",
      "slug": "macbook-pro-m3"
    }
  ]
}
```

---

# 6. Get Compare List

## Endpoint

```
GET /api/v1/users/me/compare-list
```

## Description

Lấy danh sách sản phẩm đang được user thêm vào compare.

## Authentication

Yêu cầu JWT.

## Success Response

```json
{
  "success": true,
  "message": "Compare list retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "iPhone 15",
      "slug": "iphone-15"
    },
    {
      "id": 3,
      "name": "Samsung Galaxy S24",
      "slug": "samsung-galaxy-s24"
    }
  ]
}
```

---

# 7. User API Summary

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| /api/v1/users/me | GET | Lấy profile hiện tại | Yes |
| /api/v1/users/me | PUT | Cập nhật profile | Yes |
| /api/v1/users/change-password | POST | Đổi mật khẩu | Yes |
| /api/v1/users/me/favorites | GET | Lấy danh sách yêu thích | Yes |
| /api/v1/users/me/compare-list | GET | Lấy danh sách compare | Yes |