# Post Module

## 1. Purpose

Post Module quản lý **bài viết blog công nghệ** trong hệ thống Tech Blog Java.

Module này đóng vai trò CMS cho nội dung editorial và có workflow kiểm duyệt trước khi publish.

---

# 2. Responsibilities

Post Module chịu trách nhiệm:

- tạo bài viết
- cập nhật bài viết
- submit bài viết
- approve / reject / hide bài viết
- quản lý view count
- hỗ trợ search
- hỗ trợ featured content
- lưu moderation logs

---

# 3. Main Features

## 3.1 Post CRUD

Author/Admin tạo và cập nhật bài viết.

## 3.2 Workflow

Post đi qua các trạng thái:

- DRAFT
- PENDING
- PUBLISHED
- REJECTED
- HIDDEN

## 3.3 Moderation

Admin duyệt hoặc từ chối bài viết.

## 3.4 Search

Cho phép tìm kiếm bài viết theo keyword.

## 3.5 Featured

Admin có thể đánh dấu bài viết nổi bật.

---

# 4. Main Packages

```text
domain/post
├── controller
├── dto
├── mapper
├── model
├── repository
└── service
```

---

# 5. Main APIs

- `GET /api/v1/posts`
- `GET /api/v1/posts/{id}`
- `POST /api/v1/posts`
- `PUT /api/v1/posts/{id}`
- `PATCH /api/v1/posts/{id}/submit`
- `PATCH /api/v1/posts/{id}/approve`
- `PATCH /api/v1/posts/{id}/reject`
- `PATCH /api/v1/posts/{id}/hide`
- `DELETE /api/v1/posts/{id}`
- `GET /api/v1/posts/search`
- `PATCH /api/v1/posts/{id}/featured`
- `GET /api/v1/posts/{id}/moderation-logs`

---

# 6. Main Data Involved

Các bảng liên quan:

- posts
- post_moderation_logs

Liên kết với:

- categories
- users
- comments

---

# 7. Business Rules

- post phải thuộc category hợp lệ
- post phải có title, content
- slug phải unique
- author tạo post ở trạng thái DRAFT
- muốn public phải submit và được admin approve
- post hidden không hiển thị public
- moderation actions phải được log lại

---

# 8. Dependencies

Post Module phụ thuộc vào:

- User Module
- Category Module
- Security package

Comment Module phụ thuộc vào Post Module khi comment target là post.

---

# 9. Suggested Service Methods

- `getPosts(PostFilterRequest request)`
- `getPostById(Long id)`
- `createPost(CreatePostRequest request)`
- `updatePost(Long id, UpdatePostRequest request)`
- `submitPost(Long id)`
- `approvePost(Long id)`
- `rejectPost(Long id, RejectRequest request)`
- `hidePost(Long id)`
- `deletePost(Long id)`
- `searchPosts(PostSearchRequest request)`
- `updateFeaturedStatus(Long id, FeaturedRequest request)`
- `getModerationLogs(Long id)`

---

# 10. Error Cases

- post không tồn tại
- category không tồn tại
- slug trùng
- user không phải owner hoặc admin
- trạng thái không hợp lệ cho action hiện tại
- thiếu quyền moderation

---

# 11. Development Notes

- nên tách response list và detail
- search có thể bắt đầu đơn giản bằng title/summary/content
- moderation log nên lưu action, moderator, reason, createdAt
- view count nên cập nhật cẩn thận để tránh duplicate count quá mức

---

# 12. Definition of Done

Post Module được xem là hoàn thành khi:

- CRUD hoạt động
- workflow submit/approve/reject/hide hoạt động
- moderation logs ghi nhận đúng
- search hoạt động ở mức cơ bản
- featured flag hoạt động
- permission đúng cho author/admin