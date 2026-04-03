# Phân Công Nhóm (Team Assignments)

Dự án chia thành các module cốt lõi độc lập nhưng có tính liên kết tương phối trong hệ sinh thái ứng dụng:

- **Module 1: Core Auth & User**: Xây dựng luồng Đăng nhập, Đăng ký, JWT, Spring Security, API quản lý User Profile, Filter kiểm tra quyền (RBAC).
- **Module 2: Product Core**: Cấu trúc dữ liệu Sản phẩm trên MongoDB, API danh mục (Categories), Thư viện ảnh, Thông số kỹ thuật dạng Key-Value (Specs), Điểm sao Rating.
- **Module 3: Content CMS**: API CRUD Bài viết (Posts) và Review, lưu Moderation Logs, luồng Workspace kiểm duyệt DRAFT -> PENDING -> PUBLISHED.
- **Module 4: Interaction**: API xử lý Bình luận nhánh (Nested Comments), Report, năng Yêu thích (Favorite) và So sánh sản phẩm (Compare).
- **Module 5: AI & Analytics**: API gọi mô hình AI Sentiment ngầm định đánh giá Comment, cron job tổng hợp dữ liệu, Dashboard tổng quan cho Admin.
