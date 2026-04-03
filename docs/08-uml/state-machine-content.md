# State Machine - Content Workflow

## 1. Purpose

Tài liệu này mô tả **state machine cho các nội dung chính** trong hệ thống Tech Blog Java.

Mục tiêu:

- chuẩn hóa các trạng thái nội dung
- mô tả các chuyển trạng thái hợp lệ
- làm cơ sở cho business logic và moderation workflow

Các đối tượng có state machine chính:

- Post
- Review
- Comment
- Comment Report
- Product

---

# 2. Post State Machine

## 2.1 States

- DRAFT
- PENDING
- PUBLISHED
- REJECTED
- HIDDEN

## 2.2 Transitions

```text
DRAFT
 └── submit → PENDING

PENDING
 ├── approve → PUBLISHED
 └── reject → REJECTED

PUBLISHED
 └── hide → HIDDEN
```

## 2.3 Notes

- Author tạo post ở trạng thái `DRAFT`
- Chỉ admin mới được approve hoặc reject
- `REJECTED` có thể được author chỉnh sửa và submit lại nếu policy cho phép
- `HIDDEN` dùng khi nội dung đã public nhưng bị ẩn khỏi public view

---

# 3. Review State Machine

## 3.1 States

- DRAFT
- PENDING
- PUBLISHED
- REJECTED
- HIDDEN

## 3.2 Transitions

```text
DRAFT
 └── submit → PENDING

PENDING
 ├── approve → PUBLISHED
 └── reject → REJECTED

PUBLISHED
 └── hide → HIDDEN
```

## 3.3 Notes

- Review hoạt động gần giống Post
- Review gắn với product nên cần validate product trước khi submit
- Mọi moderation action nên được log

---

# 4. Comment State Machine

## 4.1 States

- PENDING
- APPROVED
- HIDDEN
- DELETED

## 4.2 Transitions

```text
PENDING
 ├── approve → APPROVED
 ├── hide → HIDDEN
 └── delete → DELETED

APPROVED
 ├── hide → HIDDEN
 └── delete → DELETED

HIDDEN
 └── approve → APPROVED   (nếu policy cho phép)

DELETED
 └── no transition
```

## 4.3 Notes

- comment mới có thể vào `PENDING` nếu hệ thống cần moderation
- nếu hệ thống cho public comment ngay thì comment có thể vào `APPROVED` trực tiếp
- `DELETED` thường là trạng thái cuối
- admin có thể hide comment thay vì xóa cứng

---

# 5. Comment Report State Machine

## 5.1 States

- OPEN
- RESOLVED
- REJECTED

## 5.2 Transitions

```text
OPEN
 ├── resolve → RESOLVED
 └── reject → REJECTED

RESOLVED
 └── no transition

REJECTED
 └── no transition
```

## 5.3 Notes

- report mới luôn ở trạng thái `OPEN`
- admin xử lý bằng cách:
    - chấp nhận report và giải quyết → `RESOLVED`
    - từ chối report → `REJECTED`

---

# 6. Product State Machine

## 6.1 States

- DRAFT
- PUBLISHED
- HIDDEN

## 6.2 Transitions

```text
DRAFT
 └── publish → PUBLISHED

PUBLISHED
 └── hide → HIDDEN

HIDDEN
 └── publish → PUBLISHED
```

## 6.3 Notes

- product không cần workflow moderation nhiều bước như post/review
- admin có thể publish hoặc hide trực tiếp
- hidden product không nên hiển thị public

---

# 7. Textual Overview of All State Machines

```text
Post:
DRAFT -> PENDING -> PUBLISHED -> HIDDEN
                  └-> REJECTED

Review:
DRAFT -> PENDING -> PUBLISHED -> HIDDEN
                  └-> REJECTED

Comment:
PENDING -> APPROVED -> HIDDEN
   └------> DELETED
APPROVED -> DELETED

Comment Report:
OPEN -> RESOLVED
OPEN -> REJECTED

Product:
DRAFT -> PUBLISHED -> HIDDEN
HIDDEN -> PUBLISHED
```

---

# 8. Business Rules for State Changes

Các quy tắc chung:

1. Chỉ action hợp lệ mới được phép chuyển state.
2. Mỗi state transition nên được kiểm tra ở service layer.
3. Với Post/Review/Comment moderation, nên lưu log khi đổi trạng thái.
4. Không cho phép nhảy state tùy ý ngoài workflow thiết kế.

---

# 9. Service Layer Responsibility

Service layer phải chịu trách nhiệm:

- kiểm tra state hiện tại
- kiểm tra quyền của actor
- quyết định transition có hợp lệ không
- cập nhật trạng thái
- lưu moderation log nếu cần

Ví dụ:

- `submitPost()` chỉ hợp lệ khi state hiện tại là `DRAFT`
- `approveReview()` chỉ hợp lệ khi state hiện tại là `PENDING`
- `resolveReport()` chỉ hợp lệ khi report đang `OPEN`

---

# 10. Suggested Validation Method Style

Ví dụ style method:

- `validatePostCanSubmit(post)`
- `validatePostCanApprove(post)`
- `validateReviewCanReject(review)`
- `validateCommentCanHide(comment)`
- `validateReportCanResolve(report)`

Cách này giúp code dễ đọc và dễ maintain.

---

# 11. Summary

State machine của Tech Blog Java giúp:

- kiểm soát vòng đời nội dung
- chuẩn hóa moderation workflow
- tránh trạng thái không hợp lệ
- làm rõ trách nhiệm của Author, User và Admin

Các đối tượng có workflow quan trọng nhất là:

- Post
- Review
- Comment
- Comment Report
- Product