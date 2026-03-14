# Category Module

## 1. Purpose

Category Module quản lý **danh mục phân loại** cho sản phẩm và bài viết trong hệ thống Tech Blog Java.

Module này giúp tổ chức nội dung logic, hỗ trợ filter, phân loại và điều hướng.

---

# 2. Responsibilities

Category Module chịu trách nhiệm:

- tạo category
- cập nhật category
- bật / tắt category
- hỗ trợ category cha/con
- phân loại cho product hoặc post

---

# 3. Main Features

## 3.1 Create Category

Admin tạo category mới.

## 3.2 Update Category

Admin chỉnh sửa thông tin category.

## 3.3 Enable / Disable Category

Admin bật hoặc tắt category.

## 3.4 Hierarchical Category

Hệ thống hỗ trợ category cha/con thông qua `parent_id`.

---

# 4. Main Packages

```text
domain/category
├── controller
├── dto
├── model
├── repository
└── service
```

---

# 5. Main APIs

- `GET /api/v1/categories`
- `GET /api/v1/categories/{id}`
- `POST /api/v1/categories`
- `PUT /api/v1/categories/{id}`
- `PATCH /api/v1/categories/{id}/disable`
- `PATCH /api/v1/categories/{id}/enable`
- `DELETE /api/v1/categories/{id}`

---

# 6. Main Data Involved

Bảng liên quan:

- categories

Bảng này được tham chiếu bởi:

- products
- posts

---

# 7. Business Rules

- name bắt buộc
- slug phải unique
- type phải hợp lệ: PRODUCT hoặc POST
- parent_id nếu có thì phải tham chiếu category tồn tại
- category bị disable không nên hiển thị public
- category đang được dùng không nên xóa cứng nếu chưa kiểm tra ràng buộc

---

# 8. Dependencies

Category Module ít phụ thuộc module khác.

Các module phụ thuộc vào Category Module:

- Product Module
- Post Module

---

# 9. Suggested Service Methods

- `getCategories(CategoryFilterRequest request)`
- `getCategoryById(Long id)`
- `createCategory(CreateCategoryRequest request)`
- `updateCategory(Long id, UpdateCategoryRequest request)`
- `disableCategory(Long id)`
- `enableCategory(Long id)`
- `deleteCategory(Long id)`

---

# 10. Error Cases

- category không tồn tại
- slug trùng
- parent category không tồn tại
- category đang được product hoặc post sử dụng
- user không đủ quyền admin

---

# 11. Development Notes

- nên dùng slug để frontend filter thuận tiện
- nên index theo slug, type, parent_id
- cần thống nhất type enum toàn hệ thống

---

# 12. Definition of Done

Category Module được xem là hoàn thành khi:

- CRUD cơ bản hoạt động
- filter theo type hoạt động
- hỗ trợ parent-child hoạt động
- enable/disable hoạt động
- admin permission hoạt động đúng