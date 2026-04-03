# API JSON Conventions

## 1. Cấu hình định tuyến
Tiền tố mặc định của tất cả các route: `/api/v1`

## 2. Định dạng Response bắt buộc (ResponseDTO)
Ứng dụng Spring Boot sẽ tự động wrap các API payload thành thiết kế đồng nhất.

**API Thành Công:**
```json
{
  "status": 200,
  "message": "Thành công",
  "data": { ... } 
}
```

**API Gặp Lỗi / Exception:**
```json
{
  "status": 400,
  "message": "Bad Request",
  "errors": ["Validation Error: Tên sản phẩm không được rỗng"]
}
```

## 3. Quy chuẩn tham số Phân trang (Pagination)
Truyền qua Query String API: `?page=0&size=10&sort=createdAt,desc` (Spring Data Pagination format mặc định zero-based index).
Payload JSON response sẽ được bọc lại chứa metadata page total, elements count.
