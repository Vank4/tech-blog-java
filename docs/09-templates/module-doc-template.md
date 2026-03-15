# Module Documentation Template

## 1. Module Name

```text
<MODULE_NAME>
```

Ví dụ:

```text
Product Module
Auth Module
Comment Module
```

---

# 2. Purpose

Mô tả module này dùng để làm gì trong hệ thống.

Ví dụ:

- quản lý sản phẩm
- xử lý xác thực
- quản lý bình luận
- phân tích sentiment

---

# 3. Responsibilities

Liệt kê các trách nhiệm chính của module:

- <responsibility_1>
- <responsibility_2>
- <responsibility_3>

Ví dụ:

- tạo sản phẩm
- cập nhật sản phẩm
- publish / hide sản phẩm

---

# 4. Main Features

## 4.1 Feature 1

Mô tả ngắn gọn.

## 4.2 Feature 2

Mô tả ngắn gọn.

## 4.3 Feature 3

Mô tả ngắn gọn.

---

# 5. Main Packages

```text
domain/<module>
├── controller
├── dto
├── model
├── repository
└── service
```

---

# 6. Main APIs

- `<METHOD> <endpoint>`
- `<METHOD> <endpoint>`
- `<METHOD> <endpoint>`

Ví dụ:

- `GET /api/v1/products`
- `POST /api/v1/products`
- `PUT /api/v1/products/{id}`

---

# 7. Main Data Involved

Liệt kê các bảng / entity liên quan:

- <table_1>
- <table_2>
- <table_3>

Ví dụ:

- products
- product_images
- product_specs

---

# 8. Business Rules

- <rule_1>
- <rule_2>
- <rule_3>

Ví dụ:

- product phải thuộc category hợp lệ
- slug phải unique
- chỉ product đã publish mới hiển thị public

---

# 9. Dependencies

## Module này phụ thuộc vào:

- <dependency_1>
- <dependency_2>

## Module khác phụ thuộc vào module này:

- <dependent_1>
- <dependent_2>

---

# 10. Suggested Service Methods

- `<method_name_1>()`
- `<method_name_2>()`
- `<method_name_3>()`

Ví dụ:

- `createProduct()`
- `updateProduct()`
- `getProductById()`

---

# 11. Validation Rules

- <validation_rule_1>
- <validation_rule_2>
- <validation_rule_3>

---

# 12. Error Cases

- <error_case_1>
- <error_case_2>
- <error_case_3>

---

# 13. Development Notes

Ghi chú kỹ thuật:

- mapper nên tách riêng
- cần index ở DB
- cần đồng bộ với module khác
- nên lưu moderation log nếu có workflow

---

# 14. Definition of Done

Module được xem là hoàn thành khi:

- [ ] Feature 1 hoạt động
- [ ] Feature 2 hoạt động
- [ ] API test được
- [ ] Permission hoạt động đúng
- [ ] Response đúng chuẩn