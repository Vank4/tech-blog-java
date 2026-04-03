# Feat 2 - Product Image + Specs Test Guide

## Muc tieu
- Feat 2 da duoc dong bo theo chuan API cua feat 1.
- Tat ca request product/public/admin deu dung namespace `/api/v1/...`.
- Public detail dung `slug`, khong dung `id`.

## Route chuan sau khi dong bo
- Public:
  - `GET /api/v1/products`
  - `GET /api/v1/products/{slug}`
- Admin Product:
  - `POST /api/v1/admin/products`
  - `PUT /api/v1/admin/products/{id}`
  - `PATCH /api/v1/admin/products/{id}/status`
  - `DELETE /api/v1/admin/products/{id}`
  - `PATCH /api/v1/admin/products/{id}/restore`
- Admin Image + Specs:
  - `POST /api/v1/admin/products/{productId}/images`
  - `DELETE /api/v1/admin/products/{productId}/images/{imageId}`
  - `PATCH /api/v1/admin/products/{productId}/images/{imageId}/main`
  - `POST /api/v1/admin/products/{productId}/specs`
  - `PUT /api/v1/admin/products/{productId}/specs/{specId}`
  - `DELETE /api/v1/admin/products/{productId}/specs/{specId}`

## Dieu kien chuan bi
- Backend dang chay o `http://localhost:8081`
- Da co tai khoan admin seed:
  - Email: `admin@gmail.com`
  - Password: `123456`

## Buoc 1 - Login admin
### Endpoint
- `POST /api/v1/auth/login`

### JSON body
```json
{
  "email": "admin@gmail.com",
  "password": "123456"
}
```

### curl
```bash
curl --location 'http://localhost:8081/api/v1/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "admin@gmail.com",
  "password": "123456"
}'
```

## Buoc 2 - Tao product draft bang admin API
### Endpoint
- `POST /api/v1/admin/products`

### Headers
```text
Authorization: Bearer <adminToken>
Content-Type: application/json
```

### JSON body
```json
{
  "name": "iPhone 15 Pro Max",
  "slug": "iphone-15-pro-max-image-specs-demo",
  "brand": "Apple",
  "model": "A3106",
  "shortDescription": "May demo cho feat image va specs",
  "description": "San pham duoc tao de kiem thu them anh va thong so ky thuat.",
  "price": 34990000,
  "currency": "VND",
  "allowComments": true
}
```

### curl
```bash
curl --location 'http://localhost:8081/api/v1/admin/products' \
--header 'Authorization: Bearer <adminToken>' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "iPhone 15 Pro Max",
  "slug": "iphone-15-pro-max-image-specs-demo",
  "brand": "Apple",
  "model": "A3106",
  "shortDescription": "May demo cho feat image va specs",
  "description": "San pham duoc tao de kiem thu them anh va thong so ky thuat.",
  "price": 34990000,
  "currency": "VND",
  "allowComments": true
}'
```

### Response mong doi
```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 1,
    "name": "iPhone 15 Pro Max",
    "slug": "iphone-15-pro-max-image-specs-demo",
    "brand": "Apple",
    "model": "A3106",
    "shortDescription": "May demo cho feat image va specs",
    "description": "San pham duoc tao de kiem thu them anh va thong so ky thuat.",
    "price": 34990000,
    "currency": "VND",
    "status": "DRAFT",
    "allowComments": true,
    "thumbnailUrl": null,
    "images": [],
    "specs": []
  }
}
```

## Buoc 3 - Them anh chinh
### Endpoint
- `POST /api/v1/admin/products/{productId}/images`

### JSON body
```json
{
  "imageUrl": "https://example.com/images/iphone-15-pro-max-front.jpg",
  "altText": "iPhone 15 Pro Max front view",
  "primary": true,
  "displayOrder": 0
}
```

## Buoc 4 - Them anh phu
### Endpoint
- `POST /api/v1/admin/products/{productId}/images`

### JSON body
```json
{
  "imageUrl": "https://example.com/images/iphone-15-pro-max-back.jpg",
  "altText": "iPhone 15 Pro Max back view",
  "primary": false,
  "displayOrder": 1
}
```

## Buoc 5 - Dat anh phu thanh anh chinh
### Endpoint
- `PATCH /api/v1/admin/products/{productId}/images/{imageId}/main`

### curl
```bash
curl --location --request PATCH 'http://localhost:8081/api/v1/admin/products/<productId>/images/<secondaryImageId>/main' \
--header 'Authorization: Bearer <adminToken>'
```

## Buoc 6 - Them spec
### Endpoint
- `POST /api/v1/admin/products/{productId}/specs`

### JSON body
```json
{
  "specKey": "display_size",
  "specValue": "6.7",
  "unit": "inch",
  "displayOrder": 0
}
```

## Buoc 7 - Update spec
### Endpoint
- `PUT /api/v1/admin/products/{productId}/specs/{specId}`

### JSON body
```json
{
  "specKey": "display_size",
  "specValue": "6.8",
  "unit": "inch",
  "displayOrder": 0
}
```

## Buoc 8 - Publish product de test public detail
### Endpoint
- `PATCH /api/v1/admin/products/{id}/status`

### JSON body
```json
{
  "status": "PUBLISHED"
}
```

### curl
```bash
curl --location --request PATCH 'http://localhost:8081/api/v1/admin/products/<productId>/status' \
--header 'Authorization: Bearer <adminToken>' \
--header 'Content-Type: application/json' \
--data-raw '{
  "status": "PUBLISHED"
}'
```

## Buoc 9 - Verify public product detail theo slug
### Endpoint
- `GET /api/v1/products/{slug}`

### curl
```bash
curl --location 'http://localhost:8081/api/v1/products/iphone-15-pro-max-image-specs-demo'
```

### Response mong doi
```json
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "id": 1,
    "name": "iPhone 15 Pro Max",
    "slug": "iphone-15-pro-max-image-specs-demo",
    "brand": "Apple",
    "model": "A3106",
    "shortDescription": "May demo cho feat image va specs",
    "description": "San pham duoc tao de kiem thu them anh va thong so ky thuat.",
    "price": 34990000,
    "currency": "VND",
    "status": "PUBLISHED",
    "thumbnailUrl": "https://example.com/images/iphone-15-pro-max-back.jpg",
    "images": [
      {
        "id": 11,
        "imageUrl": "https://example.com/images/iphone-15-pro-max-front.jpg",
        "altText": "iPhone 15 Pro Max front view",
        "primary": false,
        "displayOrder": 0
      },
      {
        "id": 12,
        "imageUrl": "https://example.com/images/iphone-15-pro-max-back.jpg",
        "altText": "iPhone 15 Pro Max back view",
        "primary": true,
        "displayOrder": 1
      }
    ],
    "specs": [
      {
        "id": 20,
        "specKey": "display_size",
        "specValue": "6.8",
        "unit": "inch",
        "displayOrder": 0
      }
    ]
  }
}
```

## Buoc 10 - Verify public list route da dong bo
### Endpoint
- `GET /api/v1/products?brand=Apple&sort=rating-desc`

### curl
```bash
curl --location 'http://localhost:8081/api/v1/products?brand=Apple&sort=rating-desc'
```

## Buoc 11 - Xoa spec
### Endpoint
- `DELETE /api/v1/admin/products/{productId}/specs/{specId}`

## Buoc 12 - Xoa anh cu
### Endpoint
- `DELETE /api/v1/admin/products/{productId}/images/{imageId}`

## Cac loi thuong gap
- `401 Unauthorized`: thieu token admin khi goi `/api/v1/admin/**`
- `403 Forbidden`: token khong co role `ADMIN`
- `404 Not Found`: product, image hoac spec khong ton tai
- `400 Bad Request`: body sai validation, slug trung, status khong hop le

## Thu tu test nhanh
1. Login admin
2. Tao product draft
3. Them 2 anh
4. Set anh chinh
5. Them va update spec
6. Publish product
7. Goi public detail theo slug
8. Goi public list `/api/v1/products`
9. Xoa spec va xoa anh
10. Goi lai public detail de doi chieu du lieu