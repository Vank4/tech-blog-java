# API Specification Template

## 1. Purpose

Tài liệu này là template chuẩn để mô tả một API trong hệ thống **Tech Blog Java**.

Mục tiêu:

- chuẩn hóa cách viết tài liệu API
- giúp các thành viên mô tả endpoint nhất quán
- dễ review và cập nhật khi API thay đổi

---

# 2. API Name

Tên API:

```text
<API_NAME>
```

Ví dụ:

```text
Create Product API
Login API
Approve Review API
```

---

# 3. Endpoint

```text
<METHOD> <ENDPOINT>
```

Ví dụ:

```text
POST /api/v1/products
GET /api/v1/posts/{id}
PATCH /api/v1/reviews/{id}/approve
```

---

# 4. Description

Mô tả ngắn gọn API này dùng để làm gì.

Ví dụ:

- tạo sản phẩm mới
- lấy chi tiết bài viết
- duyệt review

---

# 5. Authentication

API này có yêu cầu xác thực hay không.

Ví dụ:

```text
No
Yes - JWT required
Yes - JWT + ADMIN role required
```

---

# 6. Request Parameters

## Path Variables

| Name | Type | Required | Description |
|------|------|----------|-------------|
| <param_name> | <type> | yes/no | <description> |

## Query Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| <query_name> | <type> | yes/no | <description> |

---

# 7. Request Body

```json
{
  "<field>": "<value>"
}
```

---

# 8. Validation Rules

- `<field>` bắt buộc
- `<field>` phải unique
- `<field>` phải nằm trong khoảng hợp lệ
- `<field>` phải tham chiếu entity tồn tại

---

# 9. Success Response

```json
{
  "success": true,
  "message": "<success_message>",
  "data": {}
}
```

---

# 10. Error Cases

- resource không tồn tại
- dữ liệu không hợp lệ
- không đủ quyền truy cập
- trạng thái hiện tại không cho phép action

---

# 11. Related Business Rules

- liệt kê các rule nghiệp vụ liên quan đến API này
- nếu có workflow hoặc state machine thì ghi rõ

---

# 12. Notes

- ghi chú kỹ thuật thêm nếu cần
- ví dụ API này chỉ dùng cho admin
- hoặc API này sẽ trigger AI / notification / moderation log

---

# 13. Example Filled Template

## API Name

Create Product API

## Endpoint

```text
POST /api/v1/products
```

## Description

Tạo sản phẩm mới trong hệ thống.

## Authentication

Yes - JWT + ADMIN role required

## Request Body

```json
{
  "categoryId": 1,
  "name": "iPhone 15",
  "slug": "iphone-15",
  "brand": "Apple",
  "price": 1200
}
```

## Success Response

```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 1,
    "name": "iPhone 15"
  }
}
```