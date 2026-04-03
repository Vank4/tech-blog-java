# ERD (Entity Relationship Diagram)

## 1. Purpose

Tài liệu này mô tả **ERD – Entity Relationship Diagram** của hệ thống **Tech Blog Java Backend**.

Mục tiêu:

- Mô tả các entity chính trong database
- Thể hiện quan hệ giữa các bảng
- Làm cơ sở cho việc implement JPA entities
- Giúp team hiểu rõ mô hình dữ liệu toàn hệ thống

ERD trong tài liệu này được mô tả ở dạng text để dễ đọc, dễ chỉnh sửa và dễ đồng bộ với source code.

---

# 2. Main Entity Groups

Hệ thống được chia thành các nhóm entity chính sau:

- User & Authorization
- Category
- Product
- Rating
- Post
- Review
- Comment
- Moderation
- AI / Analytics

---

# 3. Main Entities

## 3.1 User & Authorization

- users
- roles
- user_roles

## 3.2 Content Classification

- categories

## 3.3 Product

- products
- product_images
- product_specs
- product_ratings

## 3.4 Post

- posts
- post_moderation_logs

## 3.5 Review

- reviews
- review_scores
- review_moderation_logs

## 3.6 Comment

- comments
- comment_reports
- comment_moderation_logs

## 3.7 AI / Analytics

- ai_analysis_results
- analytics_events

---

# 4. Relationship Overview

Quan hệ chính trong hệ thống:

- User và Role: Many-to-Many
- Category và Product: One-to-Many
- Category và Post: One-to-Many
- Product và ProductImage: One-to-Many
- Product và ProductSpec: One-to-Many
- Product và ProductRating: One-to-Many
- Product và Review: One-to-Many
- Post và Comment: One-to-Many
- Review và Comment: One-to-Many
- User và Comment: One-to-Many
- Comment và Comment: One-to-Many (self-reference cho reply)
- Comment và CommentReport: One-to-Many
- Post và PostModerationLog: One-to-Many
- Review và ReviewModerationLog: One-to-Many
- Comment và CommentModerationLog: One-to-Many
- Review và ReviewScore: One-to-Many

---

# 5. ERD Diagram (Text Version)

```text
roles
 └──< user_roles >── users

categories
 ├──< products
 └──< posts

products
 ├──< product_images
 ├──< product_specs
 ├──< product_ratings
 └──< reviews
        ├──< review_scores
        ├──< review_moderation_logs
        └──< comments

posts
 ├──< post_moderation_logs
 └──< comments

users
 ├──< posts
 ├──< reviews
 ├──< comments
 ├──< product_ratings
 ├──< comment_reports
 ├──< post_moderation_logs
 ├──< review_moderation_logs
 └──< comment_moderation_logs

comments
 ├──< comments (reply via parent_id)
 ├──< comment_reports
 ├──< comment_moderation_logs
 └──< ai_analysis_results
```

---

# 6. Table Relationship Details

## 6.1 users ↔ roles

Quan hệ:

- Many-to-Many

Bảng trung gian:

- user_roles

Ý nghĩa:

- Một user có thể có nhiều role
- Một role có thể được gán cho nhiều user

Ví dụ:

- USER
- AUTHOR
- ADMIN

---

## 6.2 categories → products

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một category có nhiều product
- Một product thuộc một category

---

## 6.3 categories → posts

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một category có nhiều bài viết
- Một bài viết thuộc một category

---

## 6.4 products → product_images

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một sản phẩm có nhiều hình ảnh

---

## 6.5 products → product_specs

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một sản phẩm có nhiều thông số kỹ thuật

Ví dụ:

- CPU
- RAM
- Storage
- Display

---

## 6.6 products → product_ratings

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một sản phẩm nhận nhiều lượt đánh giá từ user

Ràng buộc nghiệp vụ:

- Một user chỉ được rating một sản phẩm một lần

---

## 6.7 products → reviews

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một sản phẩm có nhiều bài review

---

## 6.8 reviews → review_scores

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một bài review có nhiều điểm thành phần

Ví dụ:

- Performance score
- Battery score
- Camera score

---

## 6.9 posts → post_moderation_logs

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một bài viết có thể trải qua nhiều hành động moderation

Ví dụ:

- submit
- approve
- reject
- hide

---

## 6.10 reviews → review_moderation_logs

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một review có thể có nhiều log kiểm duyệt

---

## 6.11 comments → comment_moderation_logs

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một comment có nhiều hành động moderation

---

## 6.12 posts → comments

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một bài viết có nhiều comment

---

## 6.13 reviews → comments

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một review có nhiều comment

---

## 6.14 comments → comments

Quan hệ:

- Self-referencing One-to-Many

Khóa:

- parent_id

Ý nghĩa:

- Comment có thể reply comment khác
- Hỗ trợ cấu trúc cây cho comment

---

## 6.15 comments → comment_reports

Quan hệ:

- One-to-Many

Ý nghĩa:

- Một comment có thể bị report nhiều lần

---

## 6.16 comments → ai_analysis_results

Quan hệ:

- One-to-One hoặc One-to-Many tùy thiết kế triển khai

Ý nghĩa:

- Một comment được AI phân tích sentiment
- Kết quả được lưu riêng để phục vụ analytics

---

# 7. Suggested Primary Keys

Tất cả các bảng chính sử dụng khóa chính:

```text
id
```

Riêng bảng trung gian `user_roles` có thể dùng:

- khóa chính tổng hợp `(user_id, role_id)`
  hoặc
- khóa chính riêng `id`

Khuyến nghị:

- dùng khóa chính riêng `id` nếu cần mở rộng metadata
- dùng composite key nếu muốn tối giản

---

# 8. Suggested Foreign Keys

Ví dụ foreign key chính:

- user_roles.user_id → users.id
- user_roles.role_id → roles.id
- products.category_id → categories.id
- posts.category_id → categories.id
- posts.author_id → users.id
- reviews.product_id → products.id
- reviews.author_id → users.id
- comments.user_id → users.id
- comments.post_id → posts.id
- comments.review_id → reviews.id
- comments.parent_id → comments.id
- product_ratings.user_id → users.id
- product_ratings.product_id → products.id
- comment_reports.comment_id → comments.id
- comment_reports.reporter_id → users.id

---

# 9. Notes on Comment Target Design

Comment có thể được gắn với nhiều loại target:

- post
- review
- product (nếu hệ thống hỗ trợ comment trực tiếp trên product)

Có 2 hướng thiết kế:

## Cách 1: nhiều foreign key nullable

Ví dụ:

- post_id
- review_id
- product_id

Ưu điểm:

- dễ query
- dễ map JPA

Nhược điểm:

- phải kiểm soát chỉ một field được set

## Cách 2: polymorphic target

Ví dụ:

- target_type
- target_id

Ưu điểm:

- linh hoạt

Nhược điểm:

- khó enforce foreign key ở mức database

Khuyến nghị cho project hiện tại:

- dùng nhiều foreign key nullable để dễ triển khai với JPA

---

# 10. Notes on AI Tables

Nếu triển khai AI sentiment tách bảng, có thể dùng:

## ai_analysis_results

Các trường gợi ý:

- id
- comment_id
- sentiment_label
- confidence_score
- raw_result
- created_at

Nếu cần analytics tổng hợp, có thể bổ sung:

## analytics_events

Các trường gợi ý:

- id
- event_type
- entity_type
- entity_id
- created_at

---

# 11. Business-Critical Constraints

Các ràng buộc quan trọng cần phản ánh trong database design:

- users.email phải unique
- roles.name phải unique
- categories.slug phải unique
- products.slug phải unique
- posts.slug phải unique
- mỗi `(user_id, product_id)` trong product_ratings phải unique
- comment reply phải tham chiếu đúng parent comment
- review phải luôn gắn với product hợp lệ

---

# 12. ERD Summary

ERD của Tech Blog Java backend xoay quanh các thực thể trung tâm:

- User
- Product
- Post
- Review
- Comment
- Category

Quan hệ quan trọng nhất trong hệ thống:

- User tạo nội dung
- Product là trung tâm của review và rating
- Comment hỗ trợ tương tác nhiều tầng
- Moderation logs giúp kiểm soát nội dung
- AI analysis bổ sung lớp phân tích cảm xúc

ERD này là nền tảng cho:

- thiết kế entity JPA
- viết repository
- chuẩn hóa API
- triển khai business rules
- tối ưu database sau này