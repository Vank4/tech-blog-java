# Post API

## 1. Purpose

Tài liệu này mô tả các API liên quan đến **post management** trong hệ thống **Tech Blog Java Backend**.

Các chức năng chính:

- xem danh sách bài viết
- xem chi tiết bài viết
- tạo bài viết
- cập nhật bài viết
- workflow kiểm duyệt bài viết
- tìm kiếm bài viết
- quản lý featured post

Base path:

```
/api/v1/posts
```

---

# 2. Get Post List

## Endpoint

```
GET /api/v1/posts
```

## Description

Lấy danh sách bài viết công khai.

## Authentication

Không yêu cầu.

## Query Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| page | int | no | Trang hiện tại |
| size | int | no | Số phần tử mỗi trang |
| sort | string | no | Sắp xếp |
| category | string | no | Lọc theo category slug |
| keyword | string | no | Tìm kiếm theo từ khóa |
| status | string | no | Lọc theo trạng thái |

## Success Response

```json
{
  "success": true,
  "message": "Posts retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "Welcome to Tech Blog",
        "slug": "welcome-tech-blog",
        "summary": "Introduction post",
        "thumbnailUrl": "https://example.com/thumb.jpg",
        "status": "PUBLISHED",
        "viewCount": 120
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1
  }
}
```

---

# 3. Get Post Detail

## Endpoint

```
GET /api/v1/posts/{id}
```

## Description

Lấy chi tiết bài viết theo ID.

## Authentication

Không yêu cầu.

## Success Response

```json
{
  "success": true,
  "message": "Post retrieved successfully",
  "data": {
    "id": 1,
    "title": "Welcome to Tech Blog",
    "slug": "welcome-tech-blog",
    "summary": "Introduction post",
    "content": "Full content here",
    "thumbnailUrl": "https://example.com/thumb.jpg",
    "status": "PUBLISHED",
    "viewCount": 120,
    "author": {
      "id": 1,
      "fullName": "Admin"
    }
  }
}
```

---

# 4. Create Post

## Endpoint

```
POST /api/v1/posts
```

## Description

Tạo bài viết mới ở trạng thái DRAFT.

## Authentication

Yêu cầu JWT và role AUTHOR hoặc ADMIN.

## Request Body

```json
{
  "categoryId": 4,
  "title": "New Tech Trends",
  "slug": "new-tech-trends",
  "summary": "Summary here",
  "content": "Full content here",
  "thumbnailUrl": "https://example.com/post-thumb.jpg"
}
```

## Validation Rules

- `categoryId` bắt buộc và phải tồn tại
- `title` bắt buộc
- `slug` bắt buộc và unique
- `content` bắt buộc

## Success Response

```json
{
  "success": true,
  "message": "Post created successfully",
  "data": {
    "id": 10,
    "title": "New Tech Trends",
    "slug": "new-tech-trends",
    "status": "DRAFT"
  }
}
```

---

# 5. Update Post

## Endpoint

```
PUT /api/v1/posts/{id}
```

## Description

Cập nhật bài viết.

## Authentication

Yêu cầu JWT và role AUTHOR hoặc ADMIN.

## Request Body

```json
{
  "categoryId": 4,
  "title": "Updated Tech Trends",
  "slug": "updated-tech-trends",
  "summary": "Updated summary",
  "content": "Updated content",
  "thumbnailUrl": "https://example.com/new-thumb.jpg"
}
```

## Success Response

```json
{
  "success": true,
  "message": "Post updated successfully",
  "data": {
    "id": 10,
    "title": "Updated Tech Trends",
    "slug": "updated-tech-trends",
    "status": "DRAFT"
  }
}
```

---

# 6. Submit Post for Moderation

## Endpoint

```
PATCH /api/v1/posts/{id}/submit
```

## Description

Chuyển bài viết từ DRAFT sang PENDING để admin duyệt.

## Authentication

Yêu cầu JWT và role AUTHOR hoặc ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Post submitted for moderation",
  "data": null
}
```

---

# 7. Approve Post

## Endpoint

```
PATCH /api/v1/posts/{id}/approve
```

## Description

Admin duyệt bài viết và chuyển sang PUBLISHED.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Post approved successfully",
  "data": null
}
```

---

# 8. Reject Post

## Endpoint

```
PATCH /api/v1/posts/{id}/reject
```

## Description

Admin từ chối bài viết.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "reason": "Content does not meet publication standards"
}
```

## Success Response

```json
{
  "success": true,
  "message": "Post rejected successfully",
  "data": null
}
```

---

# 9. Hide Post

## Endpoint

```
PATCH /api/v1/posts/{id}/hide
```

## Description

Ẩn bài viết đã publish.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Post hidden successfully",
  "data": null
}
```

---

# 10. Delete Post

## Endpoint

```
DELETE /api/v1/posts/{id}
```

## Description

Xóa bài viết.

## Authentication

Yêu cầu JWT và role AUTHOR hoặc ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Post deleted successfully",
  "data": null
}
```

---

# 11. Search Posts

## Endpoint

```
GET /api/v1/posts/search
```

## Description

Tìm kiếm bài viết theo từ khóa.

## Authentication

Không yêu cầu.

## Query Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| keyword | string | yes | Từ khóa tìm kiếm |
| page | int | no | Trang hiện tại |
| size | int | no | Số phần tử mỗi trang |

## Example

```
GET /api/v1/posts/search?keyword=iphone&page=0&size=10
```

## Success Response

```json
{
  "success": true,
  "message": "Posts search completed successfully",
  "data": {
    "content": [],
    "page": 0,
    "size": 10,
    "totalElements": 0
  }
}
```

---

# 12. Set Featured Post

## Endpoint

```
PATCH /api/v1/posts/{id}/featured
```

## Description

Đánh dấu bài viết là featured.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "featured": true,
  "priority": 1
}
```

## Success Response

```json
{
  "success": true,
  "message": "Post featured status updated successfully",
  "data": null
}
```

---

# 13. Get Post Moderation Logs

## Endpoint

```
GET /api/v1/posts/{id}/moderation-logs
```

## Description

Lấy lịch sử moderation của bài viết.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Post moderation logs retrieved successfully",
  "data": [
    {
      "id": 1,
      "action": "SUBMIT",
      "reason": null,
      "createdAt": "2026-03-14T10:00:00"
    },
    {
      "id": 2,
      "action": "APPROVE",
      "reason": null,
      "createdAt": "2026-03-14T12:00:00"
    }
  ]
}
```

---

# 14. Post API Summary

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| /api/v1/posts | GET | Lấy danh sách bài viết | No |
| /api/v1/posts/{id} | GET | Lấy chi tiết bài viết | No |
| /api/v1/posts | POST | Tạo bài viết | Author/Admin |
| /api/v1/posts/{id} | PUT | Cập nhật bài viết | Author/Admin |
| /api/v1/posts/{id}/submit | PATCH | Submit bài viết | Author/Admin |
| /api/v1/posts/{id}/approve | PATCH | Duyệt bài viết | Admin |
| /api/v1/posts/{id}/reject | PATCH | Từ chối bài viết | Admin |
| /api/v1/posts/{id}/hide | PATCH | Ẩn bài viết | Admin |
| /api/v1/posts/{id} | DELETE | Xóa bài viết | Author/Admin |
| /api/v1/posts/search | GET | Tìm kiếm bài viết | No |
| /api/v1/posts/{id}/featured | PATCH | Đặt featured | Admin |
| /api/v1/posts/{id}/moderation-logs | GET | Lấy moderation logs | Admin |