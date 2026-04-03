# Table Definitions

## 1. Purpose

Tài liệu này mô tả chi tiết các bảng chính trong database của hệ thống **Tech Blog Java Backend**.

Mục tiêu:

- Giải thích chức năng của từng bảng
- Mô tả các cột quan trọng
- Làm cơ sở cho việc implement entity và repository
- Đồng bộ cách hiểu dữ liệu giữa các thành viên trong team

Tài liệu này tập trung vào các bảng cốt lõi của hệ thống.

---

# 2. users

## Purpose

Lưu thông tin tài khoản người dùng trong hệ thống.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| email | VARCHAR | Email đăng nhập, duy nhất |
| password | VARCHAR | Mật khẩu đã hash |
| full_name | VARCHAR | Họ tên người dùng |
| avatar_url | VARCHAR | Ảnh đại diện |
| bio | TEXT | Mô tả ngắn |
| status | VARCHAR | Trạng thái user |
| email_verified | BOOLEAN | Đã xác thực email hay chưa |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

- `email` phải unique
- `password` luôn lưu ở dạng hash
- `status` có thể là `ACTIVE`, `BANNED`, `INACTIVE`

---

# 3. roles

## Purpose

Lưu danh sách role của hệ thống.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| name | VARCHAR | Tên role |
| description | VARCHAR | Mô tả role |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

Ví dụ role:

- ADMIN
- USER
- AUTHOR

---

# 4. user_roles

## Purpose

Bảng trung gian giữa users và roles.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| user_id | BIGINT | FK tới users |
| role_id | BIGINT | FK tới roles |
| created_at | DATETIME | Thời điểm tạo |

## Notes

- Một user có thể có nhiều role
- Một role có thể gán cho nhiều user

---

# 5. categories

## Purpose

Lưu danh mục cho bài viết hoặc sản phẩm.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| name | VARCHAR | Tên category |
| slug | VARCHAR | Slug duy nhất |
| type | VARCHAR | Loại category |
| parent_id | BIGINT | FK tự tham chiếu nếu là category con |
| enabled | BOOLEAN | Có hiển thị hay không |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

- Hỗ trợ category cha/con
- `type` có thể phân biệt category cho post hoặc product

---

# 6. products

## Purpose

Lưu thông tin sản phẩm công nghệ.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| category_id | BIGINT | FK tới categories |
| name | VARCHAR | Tên sản phẩm |
| slug | VARCHAR | Slug duy nhất |
| brand | VARCHAR | Thương hiệu |
| short_description | VARCHAR | Mô tả ngắn |
| description | TEXT | Mô tả chi tiết |
| price | DECIMAL | Giá sản phẩm |
| status | VARCHAR | Trạng thái sản phẩm |
| published_at | DATETIME | Thời điểm publish |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

- `status` có thể là `DRAFT`, `PUBLISHED`, `HIDDEN`
- `slug` phải unique

---

# 7. product_images

## Purpose

Lưu hình ảnh của sản phẩm.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| product_id | BIGINT | FK tới products |
| image_url | VARCHAR | URL ảnh |
| is_main | BOOLEAN | Ảnh chính hay không |
| sort_order | INT | Thứ tự hiển thị |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

- Một product có nhiều image
- Chỉ nên có một ảnh `is_main = true`

---

# 8. product_specs

## Purpose

Lưu thông số kỹ thuật của sản phẩm.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| product_id | BIGINT | FK tới products |
| spec_key | VARCHAR | Tên thông số |
| spec_value | VARCHAR | Giá trị thông số |
| sort_order | INT | Thứ tự hiển thị |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

Ví dụ:

- CPU = Apple M3
- RAM = 16GB
- Display = 14 inch

---

# 9. product_ratings

## Purpose

Lưu điểm rating sản phẩm từ user.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| product_id | BIGINT | FK tới products |
| user_id | BIGINT | FK tới users |
| rating_value | INT | Điểm rating từ 1 đến 5 |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

- Một user chỉ được rating một product một lần
- `rating_value` nằm trong khoảng 1–5

---

# 10. posts

## Purpose

Lưu bài viết blog công nghệ.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| category_id | BIGINT | FK tới categories |
| author_id | BIGINT | FK tới users |
| title | VARCHAR | Tiêu đề bài viết |
| slug | VARCHAR | Slug duy nhất |
| summary | VARCHAR | Mô tả ngắn |
| content | LONGTEXT | Nội dung bài viết |
| thumbnail_url | VARCHAR | Ảnh thumbnail |
| status | VARCHAR | Trạng thái bài viết |
| view_count | BIGINT | Số lượt xem |
| published_at | DATETIME | Thời điểm publish |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

- `status` có thể là `DRAFT`, `PENDING`, `PUBLISHED`, `REJECTED`, `HIDDEN`
- `slug` phải unique

---

# 11. post_moderation_logs

## Purpose

Lưu log kiểm duyệt bài viết.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| post_id | BIGINT | FK tới posts |
| moderator_id | BIGINT | FK tới users |
| action | VARCHAR | Hành động moderation |
| reason | VARCHAR | Lý do |
| created_at | DATETIME | Thời điểm tạo |

## Notes

Ví dụ action:

- SUBMIT
- APPROVE
- REJECT
- HIDE

---

# 12. reviews

## Purpose

Lưu bài review sản phẩm.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| product_id | BIGINT | FK tới products |
| author_id | BIGINT | FK tới users |
| title | VARCHAR | Tiêu đề review |
| summary | VARCHAR | Tóm tắt |
| pros | TEXT | Ưu điểm |
| cons | TEXT | Nhược điểm |
| content | LONGTEXT | Nội dung review |
| overall_score | DECIMAL | Điểm tổng |
| status | VARCHAR | Trạng thái review |
| published_at | DATETIME | Thời điểm publish |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

- Review luôn gắn với một product
- Review phải qua moderation trước khi public

---

# 13. review_scores

## Purpose

Lưu điểm thành phần của một review.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| review_id | BIGINT | FK tới reviews |
| criteria_name | VARCHAR | Tên tiêu chí |
| score_value | DECIMAL | Điểm của tiêu chí |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

Ví dụ:

- Performance = 9.0
- Camera = 8.5
- Battery = 8.0

---

# 14. review_moderation_logs

## Purpose

Lưu log kiểm duyệt review.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| review_id | BIGINT | FK tới reviews |
| moderator_id | BIGINT | FK tới users |
| action | VARCHAR | Hành động moderation |
| reason | VARCHAR | Lý do |
| created_at | DATETIME | Thời điểm tạo |

---

# 15. comments

## Purpose

Lưu bình luận của người dùng.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| user_id | BIGINT | FK tới users |
| post_id | BIGINT | FK tới posts, nullable |
| review_id | BIGINT | FK tới reviews, nullable |
| product_id | BIGINT | FK tới products, nullable |
| parent_id | BIGINT | FK tự tham chiếu, nullable |
| content | TEXT | Nội dung comment |
| status | VARCHAR | Trạng thái comment |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

- Comment có thể gắn với post, review hoặc product
- `parent_id` dùng cho reply
- `status` có thể là `PENDING`, `APPROVED`, `HIDDEN`, `DELETED`

---

# 16. comment_reports

## Purpose

Lưu báo cáo vi phạm đối với comment.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| comment_id | BIGINT | FK tới comments |
| reporter_id | BIGINT | FK tới users |
| reason | VARCHAR | Lý do report |
| status | VARCHAR | Trạng thái xử lý |
| created_at | DATETIME | Thời điểm tạo |
| updated_at | DATETIME | Thời điểm cập nhật |

## Notes

- Một comment có thể bị report nhiều lần
- `status` có thể là `OPEN`, `RESOLVED`, `REJECTED`

---

# 17. comment_moderation_logs

## Purpose

Lưu log kiểm duyệt comment.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| comment_id | BIGINT | FK tới comments |
| moderator_id | BIGINT | FK tới users |
| action | VARCHAR | Hành động moderation |
| reason | VARCHAR | Lý do |
| created_at | DATETIME | Thời điểm tạo |

---

# 18. ai_analysis_results

## Purpose

Lưu kết quả phân tích sentiment cho comment.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| comment_id | BIGINT | FK tới comments |
| sentiment_label | VARCHAR | Nhãn cảm xúc |
| confidence_score | DECIMAL | Độ tin cậy |
| raw_result | TEXT | Kết quả raw từ AI |
| created_at | DATETIME | Thời điểm tạo |

## Notes

Sentiment label có thể là:

- POSITIVE
- NEUTRAL
- NEGATIVE

---

# 19. analytics_events

## Purpose

Lưu sự kiện phục vụ thống kê hệ thống.

## Main Columns

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Khóa chính |
| event_type | VARCHAR | Loại sự kiện |
| entity_type | VARCHAR | Loại entity |
| entity_id | BIGINT | ID entity |
| user_id | BIGINT | FK tới users, nullable |
| created_at | DATETIME | Thời điểm tạo |

## Notes

Ví dụ event:

- VIEW_POST
- VIEW_PRODUCT
- CREATE_COMMENT
- CREATE_REVIEW

---

# 20. Audit Field Convention

Hầu hết các bảng nghiệp vụ nên có các trường:

- created_at
- updated_at

Nếu cần audit nâng cao có thể thêm:

- created_by
- updated_by

---

# 21. Summary

Các bảng cốt lõi của hệ thống gồm:

- users / roles / user_roles
- categories
- products / product_images / product_specs / product_ratings
- posts / post_moderation_logs
- reviews / review_scores / review_moderation_logs
- comments / comment_reports / comment_moderation_logs
- ai_analysis_results
- analytics_events

Cấu trúc bảng được thiết kế theo hướng:

- rõ ràng
- chuẩn relational
- dễ map với JPA
- hỗ trợ mở rộng trong tương lai