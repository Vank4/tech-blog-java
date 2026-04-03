# Exception & Response Standard

## 1. Purpose

Tài liệu này mô tả **chuẩn response và exception handling** của hệ thống backend Tech Blog Java.

Mục tiêu:

- Chuẩn hóa format response của tất cả API
- Chuẩn hóa cách xử lý lỗi
- Giúp frontend dễ xử lý response
- Giúp developer debug dễ dàng

Tất cả API trong hệ thống **phải tuân theo response format thống nhất**.

---

# 2. Standard API Response Format

Tất cả API response phải sử dụng cấu trúc JSON sau:

```
{
  "success": true,
  "message": "Request successful",
  "data": {}
}
```

Ý nghĩa các trường:

| Field | Type | Description |
|------|------|-------------|
| success | boolean | Trạng thái request |
| message | string | Thông báo |
| data | object | Dữ liệu trả về |

---

# 3. Success Response Example

Ví dụ response khi request thành công:

```
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "id": 1,
    "name": "iPhone 15",
    "price": 1200
  }
}
```

---

# 4. Error Response Format

Khi có lỗi xảy ra, API trả về:

```
{
  "success": false,
  "message": "Error message",
  "data": null
}
```

Ví dụ:

```
{
  "success": false,
  "message": "Product not found",
  "data": null
}
```

---

# 5. ApiResponse Class

Backend sử dụng class generic để chuẩn hóa response.

Ví dụ:

```
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

}
```

Ưu điểm:

- response đồng nhất
- frontend dễ parse
- dễ maintain

---

# 6. HTTP Status Codes

Hệ thống sử dụng HTTP status code chuẩn.

| Status Code | Meaning |
|-------------|--------|
| 200 | Success |
| 201 | Resource created |
| 400 | Bad request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Resource not found |
| 409 | Conflict |
| 500 | Internal server error |

---

# 7. Exception Handling Strategy

Hệ thống sử dụng **Global Exception Handler** để xử lý lỗi.

File chính:

```
GlobalExceptionHandler
```

Chức năng:

- bắt tất cả exception
- trả response chuẩn
- tránh trả stacktrace ra client

---

# 8. Global Exception Handler Flow

Luồng xử lý exception:

Controller xử lý request

↓

Service xử lý business logic

↓

Exception xảy ra

↓

GlobalExceptionHandler bắt exception

↓

Trả response chuẩn cho client

---

# 9. Common Exceptions

Các exception thường dùng trong hệ thống.

### ResourceNotFoundException

Dùng khi resource không tồn tại.

Ví dụ:

```
Product not found
User not found
Post not found
```

HTTP Status:

```
404 NOT FOUND
```

---

### BadRequestException

Dùng khi request không hợp lệ.

Ví dụ:

```
Invalid request data
Missing required field
```

HTTP Status:

```
400 BAD REQUEST
```

---

### UnauthorizedException

Dùng khi user chưa đăng nhập.

HTTP Status:

```
401 UNAUTHORIZED
```

---

### ForbiddenException

Dùng khi user không có quyền truy cập.

HTTP Status:

```
403 FORBIDDEN
```

---

### ConflictException

Dùng khi dữ liệu bị trùng.

Ví dụ:

```
Email already exists
Username already exists
```

HTTP Status:

```
409 CONFLICT
```

---

# 10. Validation Error Handling

Spring validation errors cũng được xử lý qua GlobalExceptionHandler.

Ví dụ request lỗi:

```
POST /api/products
```

Request body:

```
{
  "name": ""
}
```

Response:

```
{
  "success": false,
  "message": "Validation failed",
  "data": null
}
```

---

# 11. Logging Exceptions

Khi exception xảy ra hệ thống phải log lỗi.

Ví dụ:

```
ERROR ProductService - Product not found: id=10
```

Mục tiêu:

- debug lỗi
- tracking system errors

---

# 12. Best Practices

Các nguyên tắc xử lý exception:

1. Không trả stacktrace cho client.
2. Luôn trả message rõ ràng.
3. Tất cả API phải dùng ApiResponse.
4. Tất cả exception phải đi qua GlobalExceptionHandler.
5. Log lỗi server để debug.

---

# 13. Example Controller Response

Ví dụ controller:

```
@GetMapping("/{id}")
public ApiResponse<ProductResponse> getProduct(@PathVariable Long id) {
    ProductResponse product = productService.getProductById(id);
    return new ApiResponse<>(true, "Product retrieved successfully", product);
}
```

Response:

```
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "id": 1,
    "name": "MacBook Pro"
  }
}
```

---

# 14. Summary

Hệ thống sử dụng chuẩn response thống nhất:

```
{
  "success": boolean,
  "message": string,
  "data": object
}
```

Cơ chế xử lý lỗi:

- GlobalExceptionHandler
- Custom Exceptions
- Standard HTTP status codes

Chuẩn này giúp:

- API dễ sử dụng
- frontend dễ xử lý
- code backend rõ ràng và nhất quán