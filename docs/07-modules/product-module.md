# Product Module

## 1. Purpose

Product Module là module trung tâm quản lý **thông tin sản phẩm công nghệ** trong hệ thống Tech Blog Java.

Đây là module lõi vì nhiều module khác như rating, review, compare, AI sentiment đều xoay quanh product.

---

# 2. Responsibilities

Product Module chịu trách nhiệm:

- tạo, cập nhật, xóa sản phẩm
- publish / hide sản phẩm
- quản lý ảnh sản phẩm
- quản lý thông số kỹ thuật
- hiển thị danh sách và chi tiết sản phẩm
- hỗ trợ lọc sản phẩm
- hỗ trợ compare và rating entry points

---

# 3. Main Features

## 3.1 Product CRUD

Admin có thể:

- tạo sản phẩm
- cập nhật sản phẩm
- xóa sản phẩm
- publish sản phẩm
- hide sản phẩm

## 3.2 Product Images

Quản lý nhiều ảnh cho một sản phẩm.

## 3.3 Product Specs

Quản lý danh sách thông số kỹ thuật động.

## 3.4 Product Listing

Người dùng có thể:

- xem danh sách sản phẩm
- lọc theo category
- lọc theo brand
- sắp xếp theo tiêu chí

## 3.5 Product Detail

Trang chi tiết sản phẩm hiển thị:

- thông tin cơ bản
- ảnh
- specs
- rating
- reviews
- sentiment summary

---

# 4. Main Packages

```text
domain/product
├── controller
├── dto
├── mapper
├── model
├── repository
└── service
```

---

# 5. Main APIs

- `GET /api/v1/products`
- `GET /api/v1/products/{id}`
- `POST /api/v1/products`
- `PUT /api/v1/products/{id}`
- `PATCH /api/v1/products/{id}/publish`
- `PATCH /api/v1/products/{id}/hide`
- `DELETE /api/v1/products/{id}`
- `POST /api/v1/products/{id}/images`
- `DELETE /api/v1/products/{productId}/images/{imageId}`
- `POST /api/v1/products/{id}/specs`
- `PUT /api/v1/products/{productId}/specs/{specId}`
- `DELETE /api/v1/products/{productId}/specs/{specId}`

---

# 6. Main Data Involved

Các bảng liên quan:

- products
- product_images
- product_specs
- product_ratings

Liên kết mạnh với:

- categories
- reviews
- comments
- ai_analysis_results

---

# 7. Business Rules

- product phải thuộc category hợp lệ
- slug của product phải unique
- một product có thể có nhiều ảnh
- một product chỉ nên có một ảnh chính
- product có thể có nhiều specs
- chỉ product đã publish mới hiển thị public

---

# 8. Dependencies

Product Module phụ thuộc vào:

- Category Module
- User/Security cho admin permission

Các module phụ thuộc vào Product Module:

- Rating Module
- Review Module
- Comment Module
- AI Module
- Analytics Module

---

# 9. Suggested Service Methods

- `getProducts(ProductFilterRequest request)`
- `getProductById(Long id)`
- `createProduct(CreateProductRequest request)`
- `updateProduct(Long id, UpdateProductRequest request)`
- `publishProduct(Long id)`
- `hideProduct(Long id)`
- `deleteProduct(Long id)`
- `addProductImage(Long productId, AddProductImageRequest request)`
- `removeProductImage(Long productId, Long imageId)`
- `addProductSpec(Long productId, AddProductSpecRequest request)`
- `updateProductSpec(Long productId, Long specId, UpdateProductSpecRequest request)`
- `deleteProductSpec(Long productId, Long specId)`

---

# 10. Validation Rules

- name bắt buộc
- slug bắt buộc và unique
- categoryId bắt buộc
- price >= 0
- specKey/specValue không được rỗng
- imageUrl phải hợp lệ nếu có validate URL

---

# 11. Error Cases

- product không tồn tại
- category không tồn tại
- slug trùng
- image hoặc spec không thuộc product
- user không đủ quyền admin

---

# 12. Development Notes

- nên dùng mapper để tách entity và response DTO
- cần index theo category, slug, status, brand
- nên chuẩn bị response đủ giàu để frontend product detail dễ dùng
- khi publish/hide nên audit nếu cần

---

# 13. Definition of Done

Product Module được xem là hoàn thành khi:

- CRUD hoạt động
- ảnh và specs quản lý được
- list và detail hoạt động
- publish/hide hoạt động
- response đúng chuẩn
- admin permission hoạt động đúng