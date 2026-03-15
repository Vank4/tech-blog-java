# Indexes and Constraints

## 1. Purpose

Tài liệu này mô tả các **indexes** và **constraints** quan trọng trong database của hệ thống **Tech Blog Java Backend**.

Mục tiêu:

- Đảm bảo tính toàn vẹn dữ liệu
- Tăng hiệu năng truy vấn
- Hạn chế dữ liệu trùng lặp
- Làm rõ các ràng buộc ở mức database

---

# 2. Constraint Types Used

Hệ thống sử dụng các loại constraint sau:

- Primary Key
- Foreign Key
- Unique Constraint
- Not Null Constraint
- Check Constraint
- Default Value

---

# 3. Primary Keys

Tất cả các bảng chính đều sử dụng khóa chính:

```text
id
```

Ví dụ:

- users.id
- products.id
- posts.id
- comments.id

Yêu cầu:

- không null
- duy nhất
- auto increment

---

# 4. Foreign Keys

Foreign key được sử dụng để đảm bảo referential integrity.

## Main Foreign Keys

| Table | Column | Reference |
|------|--------|-----------|
| user_roles | user_id | users.id |
| user_roles | role_id | roles.id |
| categories | parent_id | categories.id |
| products | category_id | categories.id |
| product_images | product_id | products.id |
| product_specs | product_id | products.id |
| product_ratings | product_id | products.id |
| product_ratings | user_id | users.id |
| posts | category_id | categories.id |
| posts | author_id | users.id |
| post_moderation_logs | post_id | posts.id |
| post_moderation_logs | moderator_id | users.id |
| reviews | product_id | products.id |
| reviews | author_id | users.id |
| review_scores | review_id | reviews.id |
| review_moderation_logs | review_id | reviews.id |
| review_moderation_logs | moderator_id | users.id |
| comments | user_id | users.id |
| comments | post_id | posts.id |
| comments | review_id | reviews.id |
| comments | product_id | products.id |
| comments | parent_id | comments.id |
| comment_reports | comment_id | comments.id |
| comment_reports | reporter_id | users.id |
| comment_moderation_logs | comment_id | comments.id |
| comment_moderation_logs | moderator_id | users.id |
| ai_analysis_results | comment_id | comments.id |
| analytics_events | user_id | users.id |

---

# 5. Unique Constraints

Các unique constraints quan trọng:

## users

- `email` phải unique

Lý do:

- tránh trùng tài khoản
- dùng email để đăng nhập

---

## roles

- `name` phải unique

Ví dụ:

- ADMIN
- USER
- AUTHOR

---

## categories

- `slug` phải unique

---

## products

- `slug` phải unique

---

## posts

- `slug` phải unique

---

## product_ratings

Unique composite key:

```text
(user_id, product_id)
```

Lý do:

- đảm bảo một user chỉ rating một sản phẩm một lần

---

## user_roles

Có thể dùng unique composite key:

```text
(user_id, role_id)
```

Lý do:

- tránh gán trùng role cho user

---

# 6. Not Null Constraints

Các cột bắt buộc không được null.

## users

- email
- password
- full_name
- status

## roles

- name

## categories

- name
- slug
- type

## products

- category_id
- name
- slug
- status

## product_images

- product_id
- image_url

## product_specs

- product_id
- spec_key
- spec_value

## product_ratings

- product_id
- user_id
- rating_value

## posts

- category_id
- author_id
- title
- slug
- content
- status

## reviews

- product_id
- author_id
- title
- content
- overall_score
- status

## review_scores

- review_id
- criteria_name
- score_value

## comments

- user_id
- content
- status

## comment_reports

- comment_id
- reporter_id
- reason
- status

---

# 7. Check Constraints

Check constraint được dùng để giới hạn dữ liệu hợp lệ.

## product_ratings.rating_value

Giá trị hợp lệ:

```text
1 <= rating_value <= 5
```

---

## review_scores.score_value

Giá trị hợp lệ nên nằm trong khoảng:

```text
0 <= score_value <= 10
```

---

## reviews.overall_score

Giá trị hợp lệ:

```text
0 <= overall_score <= 10
```

---

## status fields

Các cột status nên giới hạn theo enum hệ thống.

Ví dụ:

### users.status

- ACTIVE
- BANNED
- INACTIVE

### posts.status

- DRAFT
- PENDING
- PUBLISHED
- REJECTED
- HIDDEN

### reviews.status

- DRAFT
- PENDING
- PUBLISHED
- REJECTED
- HIDDEN

### comments.status

- PENDING
- APPROVED
- HIDDEN
- DELETED

### comment_reports.status

- OPEN
- RESOLVED
- REJECTED

---

# 8. Default Values

Một số cột nên có giá trị mặc định.

## users

- email_verified = false

## categories

- enabled = true

## products

- status = DRAFT

## posts

- status = DRAFT
- view_count = 0

## reviews

- status = DRAFT

## comments

- status = PENDING

## product_images

- is_main = false
- sort_order = 0

## product_specs

- sort_order = 0

---

# 9. Recommended Indexes

Indexes giúp tăng tốc truy vấn thường xuyên.

## users

- index trên `email`
- index trên `status`

---

## roles

- index trên `name`

---

## categories

- index trên `slug`
- index trên `parent_id`
- index trên `type`

---

## products

- index trên `category_id`
- index trên `slug`
- index trên `brand`
- index trên `status`
- index trên `published_at`

---

## product_images

- index trên `product_id`
- index trên `(product_id, is_main)`

---

## product_specs

- index trên `product_id`

---

## product_ratings

- unique index trên `(user_id, product_id)`
- index trên `product_id`

---

## posts

- index trên `category_id`
- index trên `author_id`
- index trên `slug`
- index trên `status`
- index trên `published_at`

---

## post_moderation_logs

- index trên `post_id`
- index trên `moderator_id`
- index trên `created_at`

---

## reviews

- index trên `product_id`
- index trên `author_id`
- index trên `status`
- index trên `published_at`

---

## review_scores

- index trên `review_id`

---

## review_moderation_logs

- index trên `review_id`
- index trên `moderator_id`
- index trên `created_at`

---

## comments

- index trên `user_id`
- index trên `post_id`
- index trên `review_id`
- index trên `product_id`
- index trên `parent_id`
- index trên `status`
- index trên `created_at`

---

## comment_reports

- index trên `comment_id`
- index trên `reporter_id`
- index trên `status`

---

## comment_moderation_logs

- index trên `comment_id`
- index trên `moderator_id`
- index trên `created_at`

---

## ai_analysis_results

- unique index trên `comment_id` nếu mỗi comment chỉ có một kết quả AI
- index trên `sentiment_label`

---

## analytics_events

- index trên `event_type`
- index trên `entity_type`
- index trên `entity_id`
- index trên `user_id`
- index trên `created_at`

---

# 10. Business-Critical Constraints

Các ràng buộc nghiệp vụ quan trọng cần enforce ở mức database hoặc application.

## One User – One Rating Per Product

Enforce bằng:

```text
UNIQUE(user_id, product_id)
```

---

## Unique Email

Enforce bằng:

```text
UNIQUE(email)
```

---

## Unique Slug

Các bảng sau cần slug duy nhất:

- categories
- products
- posts

---

## Comment Parent Integrity

Nếu `parent_id` khác null thì:

- parent comment phải tồn tại
- parent comment không được là chính nó

---

## Comment Target Integrity

Đối với bảng comments:

- chỉ một trong các cột `post_id`, `review_id`, `product_id` nên được set
- logic này nên được kiểm soát thêm ở application layer

---

## Product Main Image Rule

Mỗi product chỉ nên có một image chính.

Có thể enforce bằng:

- application logic
- hoặc unique partial index nếu DB hỗ trợ

Với MySQL, rule này thường xử lý ở application layer.

---

# 11. Performance Notes

Index không nên tạo quá nhiều nếu không cần thiết.

Nguyên tắc:

- index trên cột thường filter
- index trên foreign key
- index trên cột join
- index trên cột sort phổ biến

Không nên:

- index bừa bãi trên mọi cột
- tạo quá nhiều composite index không dùng

---

# 12. Summary

Database của Tech Blog Java sử dụng:

- Primary Key cho định danh
- Foreign Key để đảm bảo liên kết
- Unique Constraint để tránh trùng dữ liệu
- Check Constraint để giới hạn giá trị hợp lệ
- Index để tối ưu hiệu năng truy vấn

Các ràng buộc quan trọng nhất:

- `users.email` unique
- `roles.name` unique
- `slug` unique cho category, product, post
- `UNIQUE(user_id, product_id)` cho product_ratings

Thiết kế này giúp hệ thống:

- an toàn dữ liệu
- truy vấn nhanh
- dễ bảo trì
- phù hợp cho backend REST API