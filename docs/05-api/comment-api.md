# Comment API

## 1. Purpose

Tài liệu này mô tả các API liên quan đến **comment management** trong hệ thống **Tech Blog Java Backend**.

Các chức năng chính:

- tạo comment
- reply comment
- lấy danh sách comment theo post / review / product
- report comment
- ẩn / xóa comment
- duyệt comment

Base path:

```
/api/v1/comments
```

---

# 2. Get Comments by Post

## Endpoint

```
GET /api/v1/comments/by-post/{postId}
```

## Description

Lấy danh sách comment của một bài viết.

## Authentication

Không yêu cầu.

## Query Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| page | int | no | Trang hiện tại |
| size | int | no | Số phần tử mỗi trang |
| status | string | no | Lọc theo trạng thái nếu cần |

## Success Response

```json
{
  "success": true,
  "message": "Comments retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "userId": 10,
        "userName": "Nguyen Van A",
        "content": "Great article",
        "status": "APPROVED",
        "parentId": null,
        "createdAt": "2026-03-14T10:00:00",
        "replies": [
          {
            "id": 2,
            "userId": 11,
            "userName": "Tran Thi B",
            "content": "I agree",
            "status": "APPROVED",
            "parentId": 1,
            "createdAt": "2026-03-14T10:10:00"
          }
        ]
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1
  }
}
```

---

# 3. Get Comments by Review

## Endpoint

```
GET /api/v1/comments/by-review/{reviewId}
```

## Description

Lấy danh sách comment của một review.

## Authentication

Không yêu cầu.

## Success Response

```json
{
  "success": true,
  "message": "Review comments retrieved successfully",
  "data": {
    "content": [],
    "page": 0,
    "size": 10,
    "totalElements": 0
  }
}
```

---

# 4. Get Comments by Product

## Endpoint

```
GET /api/v1/comments/by-product/{productId}
```

## Description

Lấy danh sách comment trực tiếp của một sản phẩm.

## Authentication

Không yêu cầu.

## Success Response

```json
{
  "success": true,
  "message": "Product comments retrieved successfully",
  "data": {
    "content": [],
    "page": 0,
    "size": 10,
    "totalElements": 0
  }
}
```

---

# 5. Create Comment

## Endpoint

```
POST /api/v1/comments
```

## Description

Tạo comment mới cho post, review hoặc product.

## Authentication

Yêu cầu JWT.

## Request Body

```json
{
  "postId": 1,
  "reviewId": null,
  "productId": null,
  "parentId": null,
  "content": "This is a comment"
}
```

## Validation Rules

- `content` bắt buộc
- chỉ một trong `postId`, `reviewId`, `productId` được phép có giá trị
- `parentId` nếu có thì phải tồn tại

## Success Response

```json
{
  "success": true,
  "message": "Comment created successfully",
  "data": {
    "id": 1,
    "content": "This is a comment",
    "status": "PENDING"
  }
}
```

## Notes

- Comment mới có thể mặc định ở trạng thái `PENDING`
- Sau đó admin sẽ duyệt sang `APPROVED`

---

# 6. Reply Comment

## Endpoint

```
POST /api/v1/comments/{id}/reply
```

## Description

Trả lời một comment đã tồn tại.

## Authentication

Yêu cầu JWT.

## Path Variables

| Name | Type | Required | Description |
|------|------|----------|-------------|
| id | long | yes | ID comment cha |

## Request Body

```json
{
  "content": "This is a reply"
}
```

## Success Response

```json
{
  "success": true,
  "message": "Reply created successfully",
  "data": {
    "id": 2,
    "parentId": 1,
    "content": "This is a reply",
    "status": "PENDING"
  }
}
```

---

# 7. Update Comment

## Endpoint

```
PUT /api/v1/comments/{id}
```

## Description

Cập nhật nội dung comment của chính user tạo comment.

## Authentication

Yêu cầu JWT.

## Request Body

```json
{
  "content": "Updated comment content"
}
```

## Success Response

```json
{
  "success": true,
  "message": "Comment updated successfully",
  "data": {
    "id": 1,
    "content": "Updated comment content"
  }
}
```

## Error Cases

- comment không tồn tại
- user không phải chủ comment
- comment đã bị ẩn hoặc xóa

---

# 8. Delete Comment

## Endpoint

```
DELETE /api/v1/comments/{id}
```

## Description

Xóa comment.  
User có thể xóa comment của mình, admin có thể xóa bất kỳ comment nào.

## Authentication

Yêu cầu JWT.

## Success Response

```json
{
  "success": true,
  "message": "Comment deleted successfully",
  "data": null
}
```

---

# 9. Report Comment

## Endpoint

```
POST /api/v1/comments/{id}/report
```

## Description

Report comment vi phạm.

## Authentication

Yêu cầu JWT.

## Request Body

```json
{
  "reason": "Spam content"
}
```

## Validation Rules

- `reason` bắt buộc
- comment phải tồn tại

## Success Response

```json
{
  "success": true,
  "message": "Comment reported successfully",
  "data": null
}
```

---

# 10. Approve Comment

## Endpoint

```
PATCH /api/v1/comments/{id}/approve
```

## Description

Admin duyệt comment.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Comment approved successfully",
  "data": null
}
```

---

# 11. Hide Comment

## Endpoint

```
PATCH /api/v1/comments/{id}/hide
```

## Description

Admin ẩn comment khỏi hệ thống public.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "reason": "Offensive language"
}
```

## Success Response

```json
{
  "success": true,
  "message": "Comment hidden successfully",
  "data": null
}
```

---

# 12. Get Comment Reports

## Endpoint

```
GET /api/v1/comments/reports
```

## Description

Lấy danh sách report comment.

## Authentication

Yêu cầu JWT và role ADMIN.

## Query Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| status | string | no | OPEN / RESOLVED / REJECTED |
| page | int | no | Trang hiện tại |
| size | int | no | Số phần tử mỗi trang |

## Success Response

```json
{
  "success": true,
  "message": "Comment reports retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "commentId": 10,
        "reporterId": 2,
        "reason": "Spam content",
        "status": "OPEN"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1
  }
}
```

---

# 13. Resolve Comment Report

## Endpoint

```
PATCH /api/v1/comments/reports/{reportId}/resolve
```

## Description

Admin xử lý report comment.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "status": "RESOLVED",
  "note": "Comment hidden due to spam"
}
```

## Success Response

```json
{
  "success": true,
  "message": "Comment report resolved successfully",
  "data": null
}
```

---

# 14. Get Comment Moderation Logs

## Endpoint

```
GET /api/v1/comments/{id}/moderation-logs
```

## Description

Lấy lịch sử moderation của comment.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Comment moderation logs retrieved successfully",
  "data": [
    {
      "id": 1,
      "action": "APPROVE",
      "reason": null,
      "createdAt": "2026-03-14T10:30:00"
    }
  ]
}
```

---

# 15. Comment API Summary

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| /api/v1/comments/by-post/{postId} | GET | Lấy comment theo post | No |
| /api/v1/comments/by-review/{reviewId} | GET | Lấy comment theo review | No |
| /api/v1/comments/by-product/{productId} | GET | Lấy comment theo product | No |
| /api/v1/comments | POST | Tạo comment mới | Yes |
| /api/v1/comments/{id}/reply | POST | Trả lời comment | Yes |
| /api/v1/comments/{id} | PUT | Cập nhật comment | Yes |
| /api/v1/comments/{id} | DELETE | Xóa comment | Yes |
| /api/v1/comments/{id}/report | POST | Report comment | Yes |
| /api/v1/comments/{id}/approve | PATCH | Duyệt comment | Admin |
| /api/v1/comments/{id}/hide | PATCH | Ẩn comment | Admin |
| /api/v1/comments/reports | GET | Lấy danh sách report | Admin |
| /api/v1/comments/reports/{reportId}/resolve | PATCH | Xử lý report | Admin |
| /api/v1/comments/{id}/moderation-logs | GET | Lấy moderation logs | Admin |