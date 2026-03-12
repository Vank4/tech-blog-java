# Vai Trò & Phân Quyền (Roles & Permissions)

Hệ thống quản lý truy cập dựa trên Role-Based Access Control (RBAC). Hệ thống được chuẩn hóa để tích hợp với Spring Security.

## 1. ADMIN (Quản trị viên)
- Toàn quyền truy cập hệ thống.
- Quản lý người dùng (Gán role, Ban/Unban).
- Duyệt bài viết, review, bình luận.
- Quản lý danh mục, sản phẩm, nội dung nổi bật (Featured).
- Cấu hình AI (AI Settings) và xem Dashboard thống kê.

## 2. AUTHOR (Tác giả)
- Truy cập vào CMS để tạo Bài viết (Post) và Bài Đánh giá (Review).
- Soạn thảo và gửi duyệt (DRAFT -> SUBMIT).
- Quản lý bình luận trên bài viết của chính mình.
- Không có quyền tự động Publish bài (phải qua Admin duyệt).

## 3. USER (Người dùng đăng nhập)
- Xem & chỉnh sửa hồ sơ cá nhân (Avatar, Bio, Đổi mật khẩu).
- Viết bình luận (Comment), Trả lời bình luận (Reply).
- Chấm điểm sao (Rating 1-5).
- Thêm bài viết/sản phẩm vào danh sách Yêu thích (Favorite) và So sánh (Compare).

## 4. GUEST (Khách vãng lai)
- Chỉ có quyền Đọc (Xem bài viết, xem sản phẩm, đọc bình luận không cần Auth).
- Tìm kiếm, xem và lọc sản phẩm.
