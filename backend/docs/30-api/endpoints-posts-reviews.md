# Endpoints: CMS Posts & Articles Review

Hỗ trợ hệ thống workflow Publish bài viết tin tức và Review cấu hình kỹ thuật sản phẩm chuyên sâu

| HTTP | Endpoint | Giải thích / Mô tả | Quyền Auth |
|---|---|---|---|
| `GET`  | `/api/v1/posts` | Query danh sách bài viết. Chỉ show records trạng thái PUBLISHED. Có param Search. | `Public` |
| `GET`  | `/api/v1/posts/{slug}` | Hiển thị bài viết chi tiết để người đọc truy cập từ public UI. | `Public` |
| `POST` | `/api/v1/author/posts` | API tạo post ban đầu (Gán status thành DRAFT mặc định). | `AUTHOR/ADMIN` |
| `PUT`  | `/api/v1/author/posts/{id}/submit`| Author chốt file và yêu cầu Admin tiến hành kiểm định (Cấp pending) | `AUTHOR/ADMIN` |
| `PUT`  | `/api/v1/admin/posts/{id}/moderate`| Thao tác Cập nhật Workflow bài: Approval (PUBLISH) / Reject (Về DRAFT). | `ADMIN` |
