# Sequence Diagram - Post Workflow

## 1. Purpose

Tài liệu này mô tả **sequence flow của Post Workflow** trong hệ thống Tech Blog Java.

Mục tiêu:

- mô tả luồng tạo bài viết
- mô tả luồng submit bài viết
- mô tả luồng admin approve / reject
- làm rõ các bước moderation

---

# 2. Main Scenario: Create Post

Actor tham gia:

- Author
- PostController
- PostService
- CategoryRepository
- PostRepository

---

# 3. Create Post Sequence

```text
Author
 → PostController: POST /api/v1/posts
PostController
 → PostService: createPost(request)
PostService
 → CategoryRepository: findById(categoryId)
CategoryRepository
 → PostService: Category
PostService
 → PostRepository: save(Post with status = DRAFT)
PostRepository
 → PostService: Post
PostService
 → PostController: PostResponse
PostController
 → Author: ApiResponse<PostResponse>
```

---

# 4. Main Scenario: Submit Post for Moderation

Actor tham gia:

- Author
- PostController
- PostService
- PostRepository
- PostModerationLogRepository

---

# 5. Submit Post Sequence

```text
Author
 → PostController: PATCH /api/v1/posts/{id}/submit
PostController
 → PostService: submitPost(id)
PostService
 → PostRepository: findById(id)
PostRepository
 → PostService: Post
PostService
 → PostService: validate owner + status == DRAFT
PostService
 → PostRepository: update status = PENDING
PostService
 → PostModerationLogRepository: save(SUBMIT log)
PostModerationLogRepository
 → PostService: Log saved
PostService
 → PostController: success
PostController
 → Author: ApiResponse
```

---

# 6. Main Scenario: Approve Post

Actor tham gia:

- Admin
- PostController
- PostService
- PostRepository
- PostModerationLogRepository

---

# 7. Approve Post Sequence

```text
Admin
 → PostController: PATCH /api/v1/posts/{id}/approve
PostController
 → PostService: approvePost(id)
PostService
 → PostRepository: findById(id)
PostRepository
 → PostService: Post
PostService
 → PostService: validate status == PENDING
PostService
 → PostRepository: update status = PUBLISHED
PostService
 → PostRepository: update publishedAt
PostService
 → PostModerationLogRepository: save(APPROVE log)
PostService
 → PostController: success
PostController
 → Admin: ApiResponse
```

---

# 8. Main Scenario: Reject Post

```text
Admin
 → PostController: PATCH /api/v1/posts/{id}/reject
PostController
 → PostService: rejectPost(id, reason)
PostService
 → PostRepository: findById(id)
PostRepository
 → PostService: Post
PostService
 → PostService: validate status == PENDING
PostService
 → PostRepository: update status = REJECTED
PostService
 → PostModerationLogRepository: save(REJECT log with reason)
PostService
 → PostController: success
PostController
 → Admin: ApiResponse
```

---

# 9. Main Scenario: Hide Post

```text
Admin
 → PostController: PATCH /api/v1/posts/{id}/hide
PostController
 → PostService: hidePost(id)
PostService
 → PostRepository: findById(id)
PostRepository
 → PostService: Post
PostService
 → PostService: validate status == PUBLISHED
PostService
 → PostRepository: update status = HIDDEN
PostService
 → PostModerationLogRepository: save(HIDE log)
PostService
 → PostController: success
PostController
 → Admin: ApiResponse
```

---

# 10. Main Scenario: View Public Post Detail

Actor tham gia:

- Guest/User
- PostController
- PostService
- PostRepository

```text
Guest/User
 → PostController: GET /api/v1/posts/{id}
PostController
 → PostService: getPostById(id)
PostService
 → PostRepository: findPublishedPostById(id)
PostRepository
 → PostService: Post
PostService
 → PostController: PostResponse
PostController
 → Guest/User: ApiResponse<PostResponse>
```

---

# 11. Post Workflow State Changes

```text
DRAFT
 └── submit → PENDING

PENDING
 ├── approve → PUBLISHED
 └── reject → REJECTED

PUBLISHED
 └── hide → HIDDEN
```

---

# 12. Validation Points

Trong PostService cần kiểm tra:

- category có tồn tại không
- user có phải owner không
- admin có quyền moderation không
- post đang ở trạng thái hợp lệ cho action đó không

---

# 13. Related Components

Các component chính:

- `PostController`
- `PostService`
- `PostRepository`
- `PostModerationLogRepository`
- `CategoryRepository`

---

# 14. Summary

Post workflow gồm 3 giai đoạn chính:

1. Author tạo bài viết ở trạng thái DRAFT
2. Author submit bài viết sang PENDING
3. Admin approve hoặc reject bài viết

Flow này đảm bảo:

- nội dung không được public trực tiếp khi chưa kiểm duyệt
- mọi hành động moderation đều được log lại
- quyền của Author và Admin được tách rõ