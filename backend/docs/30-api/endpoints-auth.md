# Endpoints: Authentication & Security

Luồng xác thực chính được bảo vệ với hệ thống JWT Filter của Spring Security.

| HTTP | Endpoint | Giải thích / Mô tả | Quyền Auth |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | Đăng ký người dùng hệ thống mới | `Public` |
| `POST` | `/api/v1/auth/login` | Login Email/Password, cấp phát JWT | `Public` |
| `POST` | `/api/v1/auth/refresh` | Generate token mới thay thế token sắp hết hạn | `Authenticated` |
| `POST` | `/api/v1/auth/forgot-password`| Cấp lại link quên mật khẩu gửi qua hộp mail. | `Public` |
| `POST` | `/api/v1/auth/reset-password` | Thay đổi mật khẩu khi có token từ Email | `Public` |
