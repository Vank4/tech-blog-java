# Class Diagram Description

## 1. Purpose

Tài liệu này mô tả **class diagram ở mức domain model** của hệ thống **Tech Blog Java**.

Mục tiêu:

- mô tả các lớp entity chính
- thể hiện quan hệ giữa các lớp
- làm cơ sở cho thiết kế JPA entities và service logic

Class diagram trong tài liệu này được mô tả theo dạng text.

---

# 2. Core Domain Classes

Các class chính trong hệ thống:

- User
- Role
- UserRole
- Category
- Product
- ProductImage
- ProductSpec
- ProductRating
- Post
- PostModerationLog
- Review
- ReviewScore
- ReviewModerationLog
- Comment
- CommentReport
- CommentModerationLog
- AiAnalysisResult
- AnalyticsEvent

---

# 3. Class Definitions

## 3.1 User

```text
User
- id: Long
- email: String
- password: String
- fullName: String
- avatarUrl: String
- bio: String
- status: UserStatus
- emailVerified: Boolean
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.2 Role

```text
Role
- id: Long
- name: RoleName
- description: String
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.3 UserRole

```text
UserRole
- id: Long
- user: User
- role: Role
- createdAt: LocalDateTime
```

---

## 3.4 Category

```text
Category
- id: Long
- name: String
- slug: String
- type: CategoryType
- parent: Category
- enabled: Boolean
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.5 Product

```text
Product
- id: Long
- category: Category
- name: String
- slug: String
- brand: String
- shortDescription: String
- description: String
- price: BigDecimal
- status: ProductStatus
- publishedAt: LocalDateTime
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.6 ProductImage

```text
ProductImage
- id: Long
- product: Product
- imageUrl: String
- isMain: Boolean
- sortOrder: Integer
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.7 ProductSpec

```text
ProductSpec
- id: Long
- product: Product
- specKey: String
- specValue: String
- sortOrder: Integer
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.8 ProductRating

```text
ProductRating
- id: Long
- product: Product
- user: User
- ratingValue: Integer
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.9 Post

```text
Post
- id: Long
- category: Category
- author: User
- title: String
- slug: String
- summary: String
- content: String
- thumbnailUrl: String
- status: PostStatus
- viewCount: Long
- publishedAt: LocalDateTime
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.10 PostModerationLog

```text
PostModerationLog
- id: Long
- post: Post
- moderator: User
- action: ModerationAction
- reason: String
- createdAt: LocalDateTime
```

---

## 3.11 Review

```text
Review
- id: Long
- product: Product
- author: User
- title: String
- summary: String
- pros: String
- cons: String
- content: String
- overallScore: BigDecimal
- status: ReviewStatus
- publishedAt: LocalDateTime
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.12 ReviewScore

```text
ReviewScore
- id: Long
- review: Review
- criteriaName: String
- scoreValue: BigDecimal
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.13 ReviewModerationLog

```text
ReviewModerationLog
- id: Long
- review: Review
- moderator: User
- action: ModerationAction
- reason: String
- createdAt: LocalDateTime
```

---

## 3.14 Comment

```text
Comment
- id: Long
- user: User
- post: Post?
- review: Review?
- product: Product?
- parent: Comment?
- content: String
- status: CommentStatus
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.15 CommentReport

```text
CommentReport
- id: Long
- comment: Comment
- reporter: User
- reason: String
- status: ReportStatus
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 3.16 CommentModerationLog

```text
CommentModerationLog
- id: Long
- comment: Comment
- moderator: User
- action: ModerationAction
- reason: String
- createdAt: LocalDateTime
```

---

## 3.17 AiAnalysisResult

```text
AiAnalysisResult
- id: Long
- comment: Comment
- sentimentLabel: SentimentLabel
- confidenceScore: BigDecimal
- rawResult: String
- createdAt: LocalDateTime
```

---

## 3.18 AnalyticsEvent

```text
AnalyticsEvent
- id: Long
- eventType: String
- entityType: String
- entityId: Long
- user: User?
- createdAt: LocalDateTime
```

---

# 4. Relationships

## User and Role

```text
User 1..* <-> *..1 Role
via UserRole
```

---

## Category and Product

```text
Category 1 ---- * Product
```

---

## Category and Post

```text
Category 1 ---- * Post
```

---

## Category Self Reference

```text
Category 1 ---- * Category
(parent -> children)
```

---

## Product and ProductImage

```text
Product 1 ---- * ProductImage
```

---

## Product and ProductSpec

```text
Product 1 ---- * ProductSpec
```

---

## Product and ProductRating

```text
Product 1 ---- * ProductRating
User 1 ---- * ProductRating
```

---

## Product and Review

```text
Product 1 ---- * Review
User 1 ---- * Review
```

---

## Review and ReviewScore

```text
Review 1 ---- * ReviewScore
```

---

## Post and PostModerationLog

```text
Post 1 ---- * PostModerationLog
User 1 ---- * PostModerationLog
```

---

## Review and ReviewModerationLog

```text
Review 1 ---- * ReviewModerationLog
User 1 ---- * ReviewModerationLog
```

---

## Comment Relationships

```text
User 1 ---- * Comment
Post 1 ---- * Comment
Review 1 ---- * Comment
Product 1 ---- * Comment
Comment 1 ---- * Comment
(parent -> replies)
```

---

## Comment Report and Moderation

```text
Comment 1 ---- * CommentReport
User 1 ---- * CommentReport

Comment 1 ---- * CommentModerationLog
User 1 ---- * CommentModerationLog
```

---

## AI Analysis

```text
Comment 1 ---- 0..1 AiAnalysisResult
```

---

# 5. Textual Class Diagram

```text
User
 ├── UserRole ── Role
 ├── Post
 ├── Review
 ├── Comment
 ├── ProductRating
 ├── PostModerationLog
 ├── ReviewModerationLog
 ├── CommentReport
 └── CommentModerationLog

Category
 ├── Category (parent-child)
 ├── Product
 └── Post

Product
 ├── ProductImage
 ├── ProductSpec
 ├── ProductRating
 ├── Review
 └── Comment

Post
 ├── PostModerationLog
 └── Comment

Review
 ├── ReviewScore
 ├── ReviewModerationLog
 └── Comment

Comment
 ├── Comment (reply)
 ├── CommentReport
 ├── CommentModerationLog
 └── AiAnalysisResult
```

---

# 6. Notes for Implementation

- phần lớn class domain sẽ map với bảng database qua JPA Entity
- các mối quan hệ nhiều-đến-một nên dùng `@ManyToOne`
- các collection lớn có thể không cần eager fetch để tránh performance issue
- DTO layer phải tách khỏi entity để tránh expose toàn bộ quan hệ

---

# 7. Summary

Class diagram của Tech Blog Java xoay quanh các thực thể trung tâm:

- User
- Category
- Product
- Post
- Review
- Comment

Các class hỗ trợ quan trọng:

- Moderation logs
- Rating
- ReviewScore
- CommentReport
- AiAnalysisResult

Class diagram này là nền tảng cho:

- thiết kế entity JPA
- thiết kế repository
- triển khai service logic
- xây dựng API response models