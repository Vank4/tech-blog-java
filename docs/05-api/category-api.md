# Category API

## 1. Purpose

Tài liệu này mô tả các API liên quan đến **category management** trong hệ thống **Tech Blog Java Backend**.

Category được sử dụng để phân loại:

- sản phẩm
- bài viết

Base path:

```
/api/v1/categories
```

---

# 2. Get Category List

## Endpoint

```
GET /api/v1/categories
```

## Description

Lấy danh sách category đang hoạt động.

## Authentication

Không yêu cầu.

## Query Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| type | string | no | Loại category: PRODUCT hoặc POST |
| parentId | long | no | Lọc category theo parent |
| enabled | boolean | no | Lọc theo trạng thái |

## Example

```
GET /api/v1/categories?type=PRODUCT
```

## Success Response

```json
{
  "success": true,
  "message": "Categories retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Smartphones",
      "slug": "smartphones",
      "type": "PRODUCT",
      "parentId": null,
      "enabled": true
    },
    {
      "id": 2,
      "name": "Laptops",
      "slug": "laptops",
      "type": "PRODUCT",
      "parentId": null,
      "enabled": true
    }
  ]
}
```

---

# 3. Get Category Detail

## Endpoint

```
GET /api/v1/categories/{id}
```

## Description

Lấy chi tiết một category theo ID.

## Authentication

Không yêu cầu.

## Path Variables

| Name | Type | Required | Description |
|------|------|----------|-------------|
| id | long | yes | ID của category |

## Success Response

```json
{
  "success": true,
  "message": "Category retrieved successfully",
  "data": {
    "id": 1,
    "name": "Smartphones",
    "slug": "smartphones",
    "type": "PRODUCT",
    "parentId": null,
    "enabled": true
  }
}
```

## Error Cases

- category không tồn tại

---

# 4. Create Category

## Endpoint

```
POST /api/v1/categories
```

## Description

Tạo category mới.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "name": "Tablets",
  "slug": "tablets",
  "type": "PRODUCT",
  "parentId": null,
  "enabled": true
}
```

## Validation Rules

- `name` bắt buộc
- `slug` bắt buộc và unique
- `type` bắt buộc
- `parentId` nếu có thì phải tồn tại

## Success Response

```json
{
  "success": true,
  "message": "Category created successfully",
  "data": {
    "id": 10,
    "name": "Tablets",
    "slug": "tablets",
    "type": "PRODUCT",
    "parentId": null,
    "enabled": true
  }
}
```

---

# 5. Update Category

## Endpoint

```
PUT /api/v1/categories/{id}
```

## Description

Cập nhật category.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "name": "Premium Tablets",
  "slug": "premium-tablets",
  "type": "PRODUCT",
  "parentId": null,
  "enabled": true
}
```

## Success Response

```json
{
  "success": true,
  "message": "Category updated successfully",
  "data": {
    "id": 10,
    "name": "Premium Tablets",
    "slug": "premium-tablets",
    "type": "PRODUCT",
    "parentId": null,
    "enabled": true
  }
}
```

---

# 6. Disable Category

## Endpoint

```
PATCH /api/v1/categories/{id}/disable
```

## Description

Tắt hiển thị category.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Category disabled successfully",
  "data": null
}
```

---

# 7. Enable Category

## Endpoint

```
PATCH /api/v1/categories/{id}/enable
```

## Description

Bật lại category đã bị disable.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Category enabled successfully",
  "data": null
}
```

---

# 8. Delete Category

## Endpoint

```
DELETE /api/v1/categories/{id}
```

## Description

Xóa category.  
Khuyến nghị chỉ cho xóa khi category chưa được sử dụng bởi post hoặc product.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Category deleted successfully",
  "data": null
}
```

## Error Cases

- category đang được sử dụng
- category không tồn tại

---

# 9. Category API Summary

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| /api/v1/categories | GET | Lấy danh sách category | No |
| /api/v1/categories/{id} | GET | Lấy chi tiết category | No |
| /api/v1/categories | POST | Tạo category mới | Admin |
| /api/v1/categories/{id} | PUT | Cập nhật category | Admin |
| /api/v1/categories/{id}/disable | PATCH | Disable category | Admin |
| /api/v1/categories/{id}/enable | PATCH | Enable category | Admin |
| /api/v1/categories/{id} | DELETE | Xóa category | Admin |