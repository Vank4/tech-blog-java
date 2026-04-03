# Comment Module

## 1. Purpose

Comment Module quản lý **bình luận và tương tác hội thoại** trong hệ thống Tech Blog Java.

Đây là module cộng đồng quan trọng, kết nối người dùng với post, review và product.

---

# 2. Responsibilities

Comment Module chịu trách nhiệm:

- tạo comment
- reply comment
- cập nhật comment
- xóa comment
- duyệt / ẩn comment
- report comment
- xử lý report
- lưu moderation logs

---

# 3. Main Features

## 3.1 Create Comment

User tạo comment mới.

## 3.2 Reply Comment

User trả lời một comment đã có.

## 3.3 Comment Tree

Comment hỗ trợ parent-child thông qua `parent_id`.

## 3.4 Moderation

Admin có thể:

- approve
- hide
- delete

## 3.5 Report

User có thể report comment vi phạm.

---

# 4. Main Packages

```text
domain/comment
├── controller
├── dto
├── model
├── repository
└── service
```

---

# 5. Main APIs

- `GET /api/v1/comments/by-post/{postId}`
- `GET /api/v1/comments/by-review/{reviewId}`
- `GET /api/v1/comments/by-product/{productId}`
- `POST /api/v1/comments`
- `POST /api/v1/comments/{id}/reply`
- `PUT /api/v1/comments/{id}`
- `DELETE /api/v1/comments/{id}`
- `POST /api/v1/comments/{id}/report`
- `PATCH /api/v1/comments/{id}/approve`
- `PATCH /api/v1/comments/{id}/hide`
- `GET /api/v1/comments/reports`
- `PATCH /api/v1/comments/reports/{reportId}/resolve`
- `GET /api/v1/comments/{id}/moderation-logs`

---

# 6. Main Data Involved

Các bảng liên quan:

- comments
- comment_reports
- comment_moderation_logs

Comment có thể tham chiếu:

- posts
- reviews
- products
- comments (self-reference)

---

# 7. Business Rules

- content của comment bắt buộc
- chỉ một target được set: post hoặc review hoặc product
- parent comment phải tồn tại nếu là reply
- comment mới có thể ở trạng thái PENDING
- admin duyệt comment sang APPROVED
- comment vi phạm có thể bị hide hoặc delete
- report phải có reason

---

# 8. Dependencies

Comment Module phụ thuộc vào:

- User Module
- Post Module
- Review Module
- Product Module
- Security package

AI Module phụ thuộc mạnh vào Comment Module để lấy dữ liệu sentiment input.

---

# 9. Suggested Service Methods

- `getCommentsByPost(Long postId, CommentFilterRequest request)`
- `getCommentsByReview(Long reviewId, CommentFilterRequest request)`
- `getCommentsByProduct(Long productId, CommentFilterRequest request)`
- `createComment(CreateCommentRequest request)`
- `replyComment(Long parentId, ReplyCommentRequest request)`
- `updateComment(Long id, UpdateCommentRequest request)`
- `deleteComment(Long id)`
- `reportComment(Long id, ReportCommentRequest request)`
- `approveComment(Long id)`
- `hideComment(Long id, HideCommentRequest request)`
- `getCommentReports(CommentReportFilterRequest request)`
- `resolveReport(Long reportId, ResolveReportRequest request)`
- `getModerationLogs(Long id)`

---

# 10. Validation Rules

- content không được rỗng
- reason report không được rỗng
- parentId nếu có phải hợp lệ
- target entity phải tồn tại
- chỉ chủ comment hoặc admin mới được sửa/xóa comment tùy policy

---

# 11. Error Cases

- comment không tồn tại
- target không tồn tại
- parent comment không tồn tại
- report không tồn tại
- user không đủ quyền
- trạng thái report không hợp lệ cho action resolve

---

# 12. Development Notes

- nên cân nhắc trả comment tree ở mức vừa phải để tránh recursion sâu
- nên index theo post_id, review_id, product_id, parent_id, status
- moderation log nên ghi rõ action và reason
- AI trigger có thể gọi sau khi comment được tạo hoặc sau khi approve tùy policy

---

# 13. Definition of Done

Comment Module được xem là hoàn thành khi:

- comment/reply hoạt động
- get comments theo target hoạt động
- report/resolve report hoạt động
- moderation hoạt động
- permission owner/admin hoạt động
- response đúng chuẩn