# Admin API

## 1. Purpose

Tài liệu này mô tả các API tổng hợp dành cho **admin operations** trong hệ thống **Tech Blog Java Backend**.

Mục tiêu:

- quản lý user
- quản lý moderation
- xem dashboard tổng quan
- thao tác quản trị cấp hệ thống

Base path:

```
/api/v1/admin
```

Tất cả API trong tài liệu này đều yêu cầu:

- JWT hợp lệ
- role ADMIN

---

# 2. Get Admin Dashboard Summary

## Endpoint

```
GET /api/v1/admin/dashboard
```

## Description

Lấy dữ liệu tổng quan cho dashboard admin.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Admin dashboard retrieved successfully",
  "data": {
    "totalUsers": 120,
    "totalProducts": 85,
    "totalPosts": 42,
    "totalReviews": 61,
    "totalComments": 540,
    "pendingPosts": 3,
    "pendingReviews": 4,
    "pendingComments": 12,
    "openReports": 7
  }
}
```

---

# 3. Get User List

## Endpoint

```
GET /api/v1/admin/users
```

## Description

Lấy danh sách user trong hệ thống.

## Authentication

Yêu cầu JWT và role ADMIN.

## Query Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| page | int | no | Trang hiện tại |
| size | int | no | Số phần tử mỗi trang |
| keyword | string | no | Tìm theo email hoặc tên |
| status | string | no | ACTIVE / BANNED / INACTIVE |

## Success Response

```json
{
  "success": true,
  "message": "Users retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "email": "admin@techblog.com",
        "fullName": "System Admin",
        "status": "ACTIVE",
        "roles": ["ADMIN"]
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1
  }
}
```

---

# 4. Get User Detail

## Endpoint

```
GET /api/v1/admin/users/{id}
```

## Description

Lấy chi tiết một user.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": 2,
    "email": "user@example.com",
    "fullName": "Nguyen Van A",
    "status": "ACTIVE",
    "emailVerified": true,
    "roles": ["USER"]
  }
}
```

---

# 5. Assign Role to User

## Endpoint

```
PATCH /api/v1/admin/users/{id}/roles
```

## Description

Cập nhật danh sách role của user.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "roles": ["USER", "AUTHOR"]
}
```

## Success Response

```json
{
  "success": true,
  "message": "User roles updated successfully",
  "data": null
}
```

---

# 6. Ban User

## Endpoint

```
PATCH /api/v1/admin/users/{id}/ban
```

## Description

Khóa tài khoản user.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "reason": "Violation of community guidelines"
}
```

## Success Response

```json
{
  "success": true,
  "message": "User banned successfully",
  "data": null
}
```

---

# 7. Unban User

## Endpoint

```
PATCH /api/v1/admin/users/{id}/unban
```

## Description

Mở khóa tài khoản user.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "User unbanned successfully",
  "data": null
}
```

---

# 8. Get Pending Posts

## Endpoint

```
GET /api/v1/admin/posts/pending
```

## Description

Lấy danh sách bài viết đang chờ duyệt.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Pending posts retrieved successfully",
  "data": {
    "content": [],
    "page": 0,
    "size": 10,
    "totalElements": 0
  }
}
```

---

# 9. Get Pending Reviews

## Endpoint

```
GET /api/v1/admin/reviews/pending
```

## Description

Lấy danh sách review đang chờ duyệt.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Pending reviews retrieved successfully",
  "data": {
    "content": [],
    "page": 0,
    "size": 10,
    "totalElements": 0
  }
}
```

---

# 10. Get Pending Comments

## Endpoint

```
GET /api/v1/admin/comments/pending
```

## Description

Lấy danh sách comment đang chờ duyệt.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Pending comments retrieved successfully",
  "data": {
    "content": [],
    "page": 0,
    "size": 10,
    "totalElements": 0
  }
}
```

---

# 11. Get Open Comment Reports

## Endpoint

```
GET /api/v1/admin/comment-reports/open
```

## Description

Lấy danh sách report comment chưa xử lý.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Open comment reports retrieved successfully",
  "data": {
    "content": [],
    "page": 0,
    "size": 10,
    "totalElements": 0
  }
}
```

---

# 12. Get Analytics Summary

## Endpoint

```
GET /api/v1/admin/analytics/summary
```

## Description

Lấy dữ liệu thống kê tổng quan.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Analytics summary retrieved successfully",
  "data": {
    "dailyActiveUsers": 45,
    "monthlyNewUsers": 30,
    "mostViewedPosts": 5,
    "mostViewedProducts": 7
  }
}
```

---

# 13. Get Featured Content

## Endpoint

```
GET /api/v1/admin/featured
```

## Description

Lấy danh sách nội dung đang được gắn featured.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Featured content retrieved successfully",
  "data": [
    {
      "targetType": "POST",
      "targetId": 10,
      "priority": 1
    }
  ]
}
```

---

# 14. Update Featured Content

## Endpoint

```
PUT /api/v1/admin/featured
```

## Description

Cập nhật danh sách featured content.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "items": [
    {
      "targetType": "POST",
      "targetId": 10,
      "priority": 1
    },
    {
      "targetType": "PRODUCT",
      "targetId": 3,
      "priority": 2
    }
  ]
}
```

## Success Response

```json
{
  "success": true,
  "message": "Featured content updated successfully",
  "data": null
}
```

---

# 15. Admin API Summary

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| /api/v1/admin/dashboard | GET | Lấy dashboard tổng quan | Admin |
| /api/v1/admin/users | GET | Lấy danh sách user | Admin |
| /api/v1/admin/users/{id} | GET | Lấy chi tiết user | Admin |
| /api/v1/admin/users/{id}/roles | PATCH | Gán role cho user | Admin |
| /api/v1/admin/users/{id}/ban | PATCH | Ban user | Admin |
| /api/v1/admin/users/{id}/unban | PATCH | Unban user | Admin |
| /api/v1/admin/posts/pending | GET | Lấy post chờ duyệt | Admin |
| /api/v1/admin/reviews/pending | GET | Lấy review chờ duyệt | Admin |
| /api/v1/admin/comments/pending | GET | Lấy comment chờ duyệt | Admin |
| /api/v1/admin/comment-reports/open | GET | Lấy report chưa xử lý | Admin |
| /api/v1/admin/analytics/summary | GET | Lấy thống kê tổng quan | Admin |
| /api/v1/admin/featured | GET | Lấy featured content | Admin |
| /api/v1/admin/featured | PUT | Cập nhật featured content | Admin |