# Endpoints: Comments & User Interaction (AI)

| HTTP | Endpoint | Giải thích / Mô tả | Quyền Auth |
|---|---|---|---|
| `GET`  | `/api/v1/comments` | Lấy danh sách comments dựa vào tham số query `targetId` | `Public` |
| `POST` | `/api/v1/comments` | Tạo viết comment (Kích hoạt luồng AI đánh giá chạy ngầm) | `USER/AUTHOR/ADMIN` |
| `POST` | `/api/v1/comments/{id}/report`| Báo cáo khi phát hiện comment vi phạm | `USER/AUTHOR/ADMIN` |
| `PUT`  | `/api/v1/admin/comments/{id}/status`| Admin thực thi quyền force ẩn comment | `ADMIN` |
| `GET`  | `/api/v1/admin/ai/settings` | Đọc cấu hình ngưỡng AI Threshold | `ADMIN` |
| `PUT`  | `/api/v1/admin/ai/settings` | Cập nhật cấu hình độ nhạy của AI qua Model | `ADMIN` |
