# Product API

## 1. Purpose

Tài liệu này mô tả các API liên quan đến **product management** trong hệ thống **Tech Blog Java Backend**.

Các chức năng chính:

- xem danh sách sản phẩm
- xem chi tiết sản phẩm
- tạo sản phẩm
- cập nhật sản phẩm
- ẩn / publish sản phẩm
- quản lý ảnh và thông số kỹ thuật
- rating sản phẩm
- compare sản phẩm

Base path:

```
/api/v1/products
```

---

# 2. Get Product List

## Endpoint

```
GET /api/v1/products
```

## Description

Lấy danh sách sản phẩm công khai.

## Authentication

Không yêu cầu.

## Query Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| page | int | no | Trang hiện tại |
| size | int | no | Số phần tử mỗi trang |
| sort | string | no | Sắp xếp |
| category | string | no | Lọc theo category slug |
| brand | string | no | Lọc theo brand |
| status | string | no | Lọc theo trạng thái |
| keyword | string | no | Từ khóa tìm kiếm |

## Example

```
GET /api/v1/products?page=0&size=10&category=smartphones&brand=Apple
```

## Success Response

```json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "iPhone 15",
        "slug": "iphone-15",
        "brand": "Apple",
        "price": 1200,
        "status": "PUBLISHED",
        "averageRating": 4.8,
        "mainImageUrl": "https://example.com/iphone15.jpg"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1
  }
}
```

---

# 3. Get Product Detail

## Endpoint

```
GET /api/v1/products/{id}
```

## Description

Lấy chi tiết sản phẩm theo ID.

## Authentication

Không yêu cầu.

## Path Variables

| Name | Type | Required | Description |
|------|------|----------|-------------|
| id | long | yes | ID sản phẩm |

## Success Response

```json
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "id": 1,
    "name": "iPhone 15",
    "slug": "iphone-15",
    "brand": "Apple",
    "price": 1200,
    "description": "Latest iPhone generation",
    "status": "PUBLISHED",
    "averageRating": 4.8,
    "images": [
      {
        "id": 1,
        "imageUrl": "https://example.com/iphone15-main.jpg",
        "isMain": true
      }
    ],
    "specs": [
      {
        "id": 1,
        "specKey": "Display",
        "specValue": "6.1 inch"
      },
      {
        "id": 2,
        "specKey": "Chip",
        "specValue": "A17 Pro"
      }
    ]
  }
}
```

---

# 4. Create Product

## Endpoint

```
POST /api/v1/products
```

## Description

Tạo sản phẩm mới.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "categoryId": 1,
  "name": "iPhone 15",
  "slug": "iphone-15",
  "brand": "Apple",
  "shortDescription": "Flagship smartphone",
  "description": "Detailed description here",
  "price": 1200,
  "status": "DRAFT"
}
```

## Validation Rules

- `categoryId` bắt buộc và phải tồn tại
- `name` bắt buộc
- `slug` bắt buộc và unique
- `brand` bắt buộc
- `price` phải >= 0

## Success Response

```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 1,
    "name": "iPhone 15",
    "slug": "iphone-15",
    "status": "DRAFT"
  }
}
```

---

# 5. Update Product

## Endpoint

```
PUT /api/v1/products/{id}
```

## Description

Cập nhật thông tin sản phẩm.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "categoryId": 1,
  "name": "iPhone 15 Pro",
  "slug": "iphone-15-pro",
  "brand": "Apple",
  "shortDescription": "Updated short description",
  "description": "Updated full description",
  "price": 1399,
  "status": "DRAFT"
}
```

## Success Response

```json
{
  "success": true,
  "message": "Product updated successfully",
  "data": {
    "id": 1,
    "name": "iPhone 15 Pro",
    "slug": "iphone-15-pro",
    "status": "DRAFT"
  }
}
```

---

# 6. Publish Product

## Endpoint

```
PATCH /api/v1/products/{id}/publish
```

## Description

Publish sản phẩm để hiển thị công khai.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Product published successfully",
  "data": null
}
```

---

# 7. Hide Product

## Endpoint

```
PATCH /api/v1/products/{id}/hide
```

## Description

Ẩn sản phẩm khỏi danh sách public.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Product hidden successfully",
  "data": null
}
```

---

# 8. Delete Product

## Endpoint

```
DELETE /api/v1/products/{id}
```

## Description

Xóa sản phẩm.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Product deleted successfully",
  "data": null
}
```

---

# 9. Add Product Image

## Endpoint

```
POST /api/v1/products/{id}/images
```

## Description

Thêm ảnh cho sản phẩm.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "imageUrl": "https://example.com/iphone15.jpg",
  "isMain": true,
  "sortOrder": 0
}
```

## Success Response

```json
{
  "success": true,
  "message": "Product image added successfully",
  "data": {
    "id": 1,
    "imageUrl": "https://example.com/iphone15.jpg",
    "isMain": true
  }
}
```

---

# 10. Remove Product Image

## Endpoint

```
DELETE /api/v1/products/{productId}/images/{imageId}
```

## Description

Xóa ảnh khỏi sản phẩm.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Product image removed successfully",
  "data": null
}
```

---

# 11. Add Product Spec

## Endpoint

```
POST /api/v1/products/{id}/specs
```

## Description

Thêm thông số kỹ thuật cho sản phẩm.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "specKey": "RAM",
  "specValue": "8GB",
  "sortOrder": 1
}
```

## Success Response

```json
{
  "success": true,
  "message": "Product spec added successfully",
  "data": {
    "id": 1,
    "specKey": "RAM",
    "specValue": "8GB"
  }
}
```

---

# 12. Update Product Spec

## Endpoint

```
PUT /api/v1/products/{productId}/specs/{specId}
```

## Description

Cập nhật thông số kỹ thuật.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "specKey": "RAM",
  "specValue": "12GB",
  "sortOrder": 1
}
```

## Success Response

```json
{
  "success": true,
  "message": "Product spec updated successfully",
  "data": {
    "id": 1,
    "specKey": "RAM",
    "specValue": "12GB"
  }
}
```

---

# 13. Delete Product Spec

## Endpoint

```
DELETE /api/v1/products/{productId}/specs/{specId}
```

## Description

Xóa thông số kỹ thuật.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Product spec deleted successfully",
  "data": null
}
```

---

# 14. Add or Update Rating

## Endpoint

```
POST /api/v1/products/{id}/ratings
```

## Description

User chấm điểm sản phẩm. Nếu đã chấm trước đó thì cập nhật điểm.

## Authentication

Yêu cầu JWT.

## Request Body

```json
{
  "ratingValue": 5
}
```

## Validation Rules

- `ratingValue` bắt buộc
- giá trị từ 1 đến 5

## Success Response

```json
{
  "success": true,
  "message": "Rating submitted successfully",
  "data": {
    "productId": 1,
    "userId": 10,
    "ratingValue": 5,
    "averageRating": 4.7
  }
}
```

---

# 15. Get Product Ratings Summary

## Endpoint

```
GET /api/v1/products/{id}/ratings
```

## Description

Lấy thống kê rating của sản phẩm.

## Authentication

Không yêu cầu.

## Success Response

```json
{
  "success": true,
  "message": "Product ratings retrieved successfully",
  "data": {
    "averageRating": 4.7,
    "totalRatings": 125
  }
}
```

---

# 16. Add Product to Compare List

## Endpoint

```
POST /api/v1/products/{id}/compare
```

## Description

Thêm sản phẩm vào danh sách compare của user.

## Authentication

Yêu cầu JWT.

## Success Response

```json
{
  "success": true,
  "message": "Product added to compare list",
  "data": null
}
```

---

# 17. Remove Product from Compare List

## Endpoint

```
DELETE /api/v1/products/{id}/compare
```

## Description

Xóa sản phẩm khỏi danh sách compare.

## Authentication

Yêu cầu JWT.

## Success Response

```json
{
  "success": true,
  "message": "Product removed from compare list",
  "data": null
}
```

---

# 18. Product API Summary

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| /api/v1/products | GET | Lấy danh sách sản phẩm | No |
| /api/v1/products/{id} | GET | Lấy chi tiết sản phẩm | No |
| /api/v1/products | POST | Tạo sản phẩm | Admin |
| /api/v1/products/{id} | PUT | Cập nhật sản phẩm | Admin |
| /api/v1/products/{id}/publish | PATCH | Publish sản phẩm | Admin |
| /api/v1/products/{id}/hide | PATCH | Ẩn sản phẩm | Admin |
| /api/v1/products/{id} | DELETE | Xóa sản phẩm | Admin |
| /api/v1/products/{id}/images | POST | Thêm ảnh sản phẩm | Admin |
| /api/v1/products/{productId}/images/{imageId} | DELETE | Xóa ảnh sản phẩm | Admin |
| /api/v1/products/{id}/specs | POST | Thêm thông số kỹ thuật | Admin |
| /api/v1/products/{productId}/specs/{specId} | PUT | Cập nhật thông số kỹ thuật | Admin |
| /api/v1/products/{productId}/specs/{specId} | DELETE | Xóa thông số kỹ thuật | Admin |
| /api/v1/products/{id}/ratings | POST | Chấm / cập nhật rating | Yes |
| /api/v1/products/{id}/ratings | GET | Lấy thống kê rating | No |
| /api/v1/products/{id}/compare | POST | Thêm vào compare | Yes |
| /api/v1/products/{id}/compare | DELETE | Xóa khỏi compare | Yes |