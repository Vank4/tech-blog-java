# Review Module

## 1. Purpose

Review Module quản lý **bài đánh giá chi tiết sản phẩm** trong hệ thống Tech Blog Java.

Review khác với product rating ở chỗ đây là nội dung editorial có cấu trúc và có moderation workflow.

---

# 2. Responsibilities

Review Module chịu trách nhiệm:

- tạo review sản phẩm
- cập nhật review
- submit review để duyệt
- approve / reject / hide review
- quản lý điểm tổng và điểm thành phần
- hiển thị review theo sản phẩm
- lưu moderation logs

---

# 3. Main Features

## 3.1 Create Review

Author/Admin tạo review cho một product.

## 3.2 Review Scores

Một review có:

- overall score
- nhiều điểm theo tiêu chí

## 3.3 Workflow

Review đi qua:

- DRAFT
- PENDING
- PUBLISHED
- REJECTED
- HIDDEN

## 3.4 Moderation

Admin duyệt hoặc từ chối review.

---

# 4. Main Packages

```text
domain/review
├── controller
├── dto
├── model
├── repository
└── service
```

---

# 5. Main APIs

- `GET /api/v1/reviews`
- `GET /api/v1/reviews/{id}`
- `POST /api/v1/reviews`
- `PUT /api/v1/reviews/{id}`
- `PATCH /api/v1/reviews/{id}/submit`
- `PATCH /api/v1/reviews/{id}/approve`
- `PATCH /api/v1/reviews/{id}/reject`
- `PATCH /api/v1/reviews/{id}/hide`
- `DELETE /api/v1/reviews/{id}`
- `GET /api/v1/reviews/by-product/{productId}`
- `GET /api/v1/reviews/{id}/moderation-logs`

---

# 6. Main Data Involved

Các bảng liên quan:

- reviews
- review_scores
- review_moderation_logs

Liên kết mạnh với:

- products
- users
- comments

---

# 7. Business Rules

- review phải gắn với product hợp lệ
- review phải có title và content
- overallScore phải hợp lệ
- điểm thành phần phải trong khoảng cho phép
- review phải được admin duyệt trước khi public
- moderation actions phải được log lại

---

# 8. Dependencies

Review Module phụ thuộc vào:

- Product Module
- User Module
- Security package

Comment Module phụ thuộc vào Review Module khi comment target là review.

AI/Analytics có thể đọc review-related stats gián tiếp nhưng không phụ thuộc trực tiếp mạnh bằng comment.

---

# 9. Suggested Service Methods

- `getReviews(ReviewFilterRequest request)`
- `getReviewById(Long id)`
- `createReview(CreateReviewRequest request)`
- `updateReview(Long id, UpdateReviewRequest request)`
- `submitReview(Long id)`
- `approveReview(Long id)`
- `rejectReview(Long id, RejectRequest request)`
- `hideReview(Long id)`
- `deleteReview(Long id)`
- `getReviewsByProduct(Long productId)`
- `getModerationLogs(Long id)`

---

# 10. Validation Rules

- productId bắt buộc
- title bắt buộc
- content bắt buộc
- overallScore nằm trong khoảng 0–10
- scoreValue của từng criteria nằm trong khoảng hợp lệ

---

# 11. Error Cases

- review không tồn tại
- product không tồn tại
- user không phải owner hoặc admin
- trạng thái hiện tại không cho phép action
- score không hợp lệ

---

# 12. Development Notes

- review score nên là child table để linh hoạt criteria
- cần đồng bộ format score cho frontend
- moderation workflow nên dùng enum rõ ràng
- nên tách review summary response và review detail response

---

# 13. Definition of Done

Review Module được xem là hoàn thành khi:

- tạo/cập nhật review hoạt động
- score component lưu được
- workflow moderation hoạt động
- get review by product hoạt động
- moderation logs hoạt động
- permission author/admin đúng