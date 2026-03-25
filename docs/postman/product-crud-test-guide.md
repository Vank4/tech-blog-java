# Hướng dẫn chi tiết Test API Product CRUD

Tài liệu này cung cấp hướng dẫn đầy đủ cách kiểm tra (test) tính năng Product CRUD từ đầu đến cuối, bao gồm cả luồng xác thực (Authentication).

## 1. Điều kiện chuẩn bị

- **Base URL**: `http://localhost:8081`
- **Cơ sở dữ liệu**: MySQL đang chạy (xampp/docker).
- **Ứng dụng**: Backend Spring Boot đang chạy ở cổng 8081.
- **Port**: 8081.

## 2. Luồng Authentication (Lấy Bearer Token)

Để test các endpoint yêu cầu quyền Admin, bạn cần lấy JWT token.

### A. Đăng nhập với tài khoản Admin (Seed sẵn)
Tài khoản admin này đã được bật sẵn `emailVerified=true` từ file `DataSeeder.java`.

- **Mục đích**: Lấy token của admin để CRUD product.
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/v1/auth/login`
- **Headers**: 
  - `Content-Type: application/json`
- **Body**:
```json
{
  "email": "admin@gmail.com",
  "password": "123456"
}
```
- **cURL chạy ngay**:
```bash
curl -X POST http://localhost:8081/api/v1/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email": "admin@gmail.com", "password": "123456"}'
```
- **Response mong đợi**:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "...",
    "user": {
      "email": "admin@gmail.com",
      "roles": ["ROLE_ADMIN"]
    }
  }
}
```
- **Cách dùng**: Lấy `accessToken` và gắn vào header: `Authorization: Bearer <token>` cho các bước sau.

---

### B. Luồng thực tế cho User thường (Đăng ký -> Verify -> Login)
Nếu bạn muốn test luồng một user mới tự đăng ký:

#### Bước 1: Đăng ký
- **URL**: `POST /api/v1/auth/register`
- **Body**:
```json
{
  "fullName": "Nguyen Van A",
  "email": "user123@gmail.com",
  "password": "password123"
}
```

#### Bước 2: Kích hoạt tài khoản (Email Verification)
Mặc định nếu chưa cấu hình SMTP, Tech Blog sẽ không gửi email thật mà log ra màn hình console.

**Có 2 cách để Verify tài khoản:**

**Cách 1: Lấy link từ Console Log (Recomended)**
1. Mở terminal đang chạy backend.
2. Tìm dòng log dạng: `Mail sender is not configured. Verification link for user123@gmail.com: http://localhost:8081/api/v1/auth/verify-email?token=...`
3. Copy toàn bộ URL đó và chạy lệnh `GET` (trong trình duyệt hoặc Postman/cURL):
```bash
curl -X GET "http://localhost:8081/api/v1/auth/verify-email?token=PHẦN_TOKEN_TRONG_LOG"
```

**Cách 2: Bypass bằng Database (Dành cho Dev)**
Nếu bạn có quyền truy cập DB (qua MySQL Workbench hoặc phpMyAdmin), hãy chạy câu lệnh SQL:
```sql
UPDATE users SET email_verified = 1 WHERE email = 'user123@gmail.com';
```

#### Bước 3: Đăng nhập sau khi verify
Sau khi verify thành công, bạn mới có thể dùng `POST /api/v1/auth/login` để lấy token. Nếu chưa verify, API sẽ trả về lỗi `400 Bad Request` với message "Email is not verified".

---

## 3. Luồng Test Product CRUD

### A. Lấy danh sách Product (Public)
- **Mục đích**: Xem tất cả sản phẩm đang ở trạng thái `PUBLISHED`.
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/products`
- **Auth**: Không yêu cầu.

### B. Lấy chi tiết Product (Public)
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/v1/products/macbook-pro-m4`
- **Auth**: Không yêu cầu.

### C. Admin tạo Product
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/v1/admin/products`
- **Auth**: `Bearer <adminToken>`
- **Body**:
```json
{
  "name": "Iphone 16 Pro Max",
  "slug": "iphone-16-pro-max",
  "brand": "Apple",
  "model": "16-PM-2024",
  "shortDescription": "Sieu pham nam 2024",
  "description": "Iphone 16 Pro Max 256GB, Pink Color.",
  "categoryId": 1,
  "price": 34990000,
  "currency": "VND",
  "allowComments": true
}
```
- **Lưu ý**: Sau khi tạo, status mặc định là `DRAFT`. Sản phẩm này **chưa hiện** ở API public.

### D. Admin cập nhật trạng thái (Publish/Hide)
- **URL**: `PATCH /api/v1/admin/products/{id}/status`
- **Body**:
```json
{
  "status": "PUBLISHED" 
}
```
- **Note**: Trạng thái hợp lệ gồm: `PUBLISHED`, `HIDDEN`.

### E. Admin cập nhật thông tin Product
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/v1/admin/products/{id}`
- **Body**: Gửi lại đầy đủ JSON như khi tạo mới nhưng có thông tin đã sửa.

### F. Admin xóa tạm (Soft Delete)
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/v1/admin/products/{id}`
- **Kết quả**: Status chuyển sang `DELETED`. Sản phẩm biến mất khỏi các API public nhưng vẫn còn trong DB.

### G. Admin khôi phục sản phẩm bị xóa
- **Method**: `PATCH`
- **URL**: `{{baseUrl}}/api/v1/admin/products/{id}/restore`
- **Kết quả**: Status trở về `DRAFT`. Muốn public lại phải gọi API Publish (mục D).

---

## 4. Các lỗi thường gặp (Troubleshooting)

| Lỗi | Nguyên nhân | Cách xử lý |
| :--- | :--- | :--- |
| **401 Unauthorized** | Không gửi Header Authorization hoặc Token hết hạn. | Thực hiện đăng nhập lại mẫu ở mục 2.A. |
| **403 Forbidden** | Dùng token user thường gọi API admin. | Đảm bảo Email login là `admin@gmail.com`. |
| **400 Email not verified** | User chưa click link verify. | Kiểm tra console log lấy link hoặc update SQL (mục 2.B). |
| **404 Not Found** | Sai Slug hoặc Product ID không tồn tại (hoặc đã bị xóa). | Check lại danh sách ID trong DB. |
| **400 Validation failed** | Thiếu `name`, `price` âm, hoặc trùng `slug`. | Kiểm tra lại JSON body gửi lên. |

---
*Tài liệu được cập nhật dựa trên thực tế triển khai repo Tech Blog Java.*