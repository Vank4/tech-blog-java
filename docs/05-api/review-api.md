# Review API

## 1. Purpose

Tài liệu này mô tả các API liên quan đến **review management** trong hệ thống **Tech Blog Java Backend**.

Các chức năng chính:

- tạo review sản phẩm
- cập nhật review
- workflow moderation review
- xem review theo sản phẩm
- quản lý điểm thành phần của review

Base path:

```
/api/v1/reviews
```

---

# 2. Get Review List

## Endpoint

```
GET /api/v1/reviews
```

## Description

Lấy danh sách review.

## Authentication

Không yêu cầu.

## Query Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| productId | long | no | Lọc theo sản phẩm |
| authorId | long | no | Lọc theo tác giả |
| status | string | no | Lọc theo trạng thái |
| page | int | no | Trang hiện tại |
| size | int | no | Số phần tử mỗi trang |

## Success Response

```json
{
  "success": true,
  "message": "Reviews retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "productId": 1,
        "title": "iPhone 15 Review",
        "overallScore": 9.0,
        "status": "PUBLISHED"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1
  }
}
```

---

# 3. Get Review Detail

## Endpoint

```
GET /api/v1/reviews/{id}
```

## Description

Lấy chi tiết review.

## Authentication

Không yêu cầu.

## Success Response

```json
{
  "success": true,
  "message": "Review retrieved successfully",
  "data": {
    "id": 1,
    "productId": 1,
    "title": "iPhone 15 Review",
    "summary": "Great phone overall",
    "pros": "Great performance, good camera",
    "cons": "High price",
    "content": "Detailed review content",
    "overallScore": 9.0,
    "status": "PUBLISHED",
    "scores": [
      {
        "criteriaName": "Performance",
        "scoreValue": 9.5
      },
      {
        "criteriaName": "Battery",
        "scoreValue": 8.5
      }
    ]
  }
}
```

---

# 4. Create Review

## Endpoint

```
POST /api/v1/reviews
```

## Description

Tạo review mới cho sản phẩm.

## Authentication

Yêu cầu JWT và role AUTHOR hoặc ADMIN.

## Request Body

```json
{
  "productId": 1,
  "title": "iPhone 15 Review",
  "summary": "Short review summary",
  "pros": "Great performance",
  "cons": "Expensive",
  "content": "Full review content",
  "overallScore": 9.0,
  "scores": [
    {
      "criteriaName": "Performance",
      "scoreValue": 9.5
    },
    {
      "criteriaName": "Battery",
      "scoreValue": 8.5
    }
  ]
}
```

## Validation Rules

- `productId` bắt buộc và phải tồn tại
- `title` bắt buộc
- `content` bắt buộc
- `overallScore` từ 0 đến 10
- mỗi `scoreValue` từ 0 đến 10

## Success Response

```json
{
  "success": true,
  "message": "Review created successfully",
  "data": {
    "id": 1,
    "title": "iPhone 15 Review",
    "status": "DRAFT"
  }
}
```

---

# 5. Update Review

## Endpoint

```
PUT /api/v1/reviews/{id}
```

## Description

Cập nhật review.

## Authentication

Yêu cầu JWT và role AUTHOR hoặc ADMIN.

## Request Body

```json
{
  "productId": 1,
  "title": "Updated iPhone 15 Review",
  "summary": "Updated summary",
  "pros": "Updated pros",
  "cons": "Updated cons",
  "content": "Updated content",
  "overallScore": 9.2,
  "scores": [
    {
      "criteriaName": "Performance",
      "scoreValue": 9.7
    }
  ]
}
```

## Success Response

```json
{
  "success": true,
  "message": "Review updated successfully",
  "data": {
    "id": 1,
    "title": "Updated iPhone 15 Review",
    "status": "DRAFT"
  }
}
```

---

# 6. Submit Review for Moderation

## Endpoint

```
PATCH /api/v1/reviews/{id}/submit
```

## Description

Chuyển review từ DRAFT sang PENDING.

## Authentication

Yêu cầu JWT và role AUTHOR hoặc ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Review submitted for moderation",
  "data": null
}
```

---

# 7. Approve Review

## Endpoint

```
PATCH /api/v1/reviews/{id}/approve
```

## Description

Admin duyệt review và publish.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Review approved successfully",
  "data": null
}
```

---

# 8. Reject Review

## Endpoint

```
PATCH /api/v1/reviews/{id}/reject
```

## Description

Admin từ chối review.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "reason": "Review content is incomplete"
}
```

## Success Response

```json
{
  "success": true,
  "message": "Review rejected successfully",
  "data": null
}
```

---

# 9. Hide Review

## Endpoint

```
PATCH /api/v1/reviews/{id}/hide
```

## Description

Ẩn review đã publish.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Review hidden successfully",
  "data": null
}
```

---

# 10. Delete Review

## Endpoint

```
DELETE /api/v1/reviews/{id}
```

## Description

Xóa review.

## Authentication

Yêu cầu JWT và role AUTHOR hoặc ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Review deleted successfully",
  "data": null
}
```

---

# 11. Get Reviews by Product

## Endpoint

```
GET /api/v1/reviews/by-product/{productId}
```

## Description

Lấy danh sách review theo sản phẩm.

## Authentication

Không yêu cầu.

## Success Response

```json
{
  "success": true,
  "message": "Product reviews retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "iPhone 15 Review",
      "overallScore": 9.0,
      "status": "PUBLISHED"
    }
  ]
}
```

---

# 12. Get Review Moderation Logs

## Endpoint

```
GET /api/v1/reviews/{id}/moderation-logs
```

## Description

Lấy lịch sử moderation của review.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Review moderation logs retrieved successfully",
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

# 13. Review API Summary

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| /api/v1/reviews | GET | Lấy danh sách review | No |
| /api/v1/reviews/{id} | GET | Lấy chi tiết review | No |
| /api/v1/reviews | POST | Tạo review | Author/Admin |
| /api/v1/reviews/{id} | PUT | Cập nhật review | Author/Admin |
| /api/v1/reviews/{id}/submit | PATCH | Submit review | Author/Admin |
| /api/v1/reviews/{id}/approve | PATCH | Duyệt review | Admin |
| /api/v1/reviews/{id}/reject | PATCH | Từ chối review | Admin |
| /api/v1/reviews/{id}/hide | PATCH | Ẩn review | Admin |
| /api/v1/reviews/{id} | DELETE | Xóa review | Author/Admin |
| /api/v1/reviews/by-product/{productId} | GET | Lấy review theo sản phẩm | No |
| /api/v1/reviews/{id}/moderation-logs | GET | Lấy moderation logs | Admin |