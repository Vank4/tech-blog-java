# Endpoints: User Management

Đóng gói các tính năng lấy và thao tác quyền người truy vấn / tài khoản.

| HTTP | Endpoint | Giải thích / Mô tả | Quyền Auth |
|---|---|---|---|
| `GET`  | `/api/v1/users/me` | Truy xuất Token claims để trả về hồ sơ My Profile Info | Thực thi ở Role bất kỳ |
| `PUT`  | `/api/v1/users/me` | Cập nhật tên, tiểu sử cá nhân và object đường dẫn Avatar. | Thực thi ở Role bất kỳ |
| `PUT`  | `/api/v1/users/me/password` | Thao tác đổi mã khoá (Cần pass mã khoá cũ để override). | Thực thi ở Role bất kỳ |
| `GET`  | `/api/v1/admin/users` | Export/Query list các định dạng người dùng (Dashboard CMS). | `ADMIN` |
| `PUT`  | `/api/v1/admin/users/{id}/role` | Phân cấp lại ủy quyền User (Thang điểm AUTHOR <-> ADMIN). | `ADMIN` |
| `PUT`  | `/api/v1/admin/users/{id}/status`| Switch Status: Khóa (Ban) người gửi sai vi phạm hoặc Mở lại kích hoạt (Active). | `ADMIN` |
