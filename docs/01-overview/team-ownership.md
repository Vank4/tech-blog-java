# Team Ownership

Dự án được chia thành 4 thành viên, mỗi thành viên phụ trách các module riêng.

---

# Member 1 – Team Leader

Responsibilities:

- Auth
- User
- Security
- Notification
- Email verification
- Reset password

Owned Modules:

- auth
- user
- security

Main APIs:

- /api/v1/auth/**
- /api/v1/users/**
- /api/v1/admin/users/**

---

# Member 2 – Product Core

Responsibilities:

- Product CRUD
- Product specs
- Product images
- Rating
- Compare

Owned Modules:

- product
- rating

Main APIs:

- /api/v1/products/**
- /api/v1/ratings/**
- /api/v1/compare/**

---

# Member 3 – Content CMS

Responsibilities:

- Categories
- Posts
- Tags
- Featured content
- Search

Owned Modules:

- category
- post

Main APIs:

- /api/v1/categories/**
- /api/v1/posts/**
- /api/v1/search/**

---

# Member 4 – Interaction + AI

Responsibilities:

- Reviews
- Comments
- Reports
- AI sentiment
- Analytics

Owned Modules:

- review
- comment
- ai
- analytics

Main APIs:

- /api/v1/reviews/**
- /api/v1/comments/**
- /api/v1/reports/**
- /api/v1/admin/ai/**

---

# Ownership Rules

- Mỗi thành viên chịu trách nhiệm chính cho module của mình
- Không chỉnh sửa module của người khác nếu không có thảo luận
- Thay đổi database schema phải thông báo team
- Pull request phải được review trước khi merge