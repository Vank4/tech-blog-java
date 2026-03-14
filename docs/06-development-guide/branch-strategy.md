# Branch Strategy

## 1. Purpose

Tài liệu này mô tả chiến lược branch cho dự án **Tech Blog Java**.

Mục tiêu:

- chuẩn hóa cách làm việc với Git
- tránh conflict không cần thiết
- giúp team làm song song dễ hơn
- đảm bảo code tích hợp an toàn

---

# 2. Main Branches

Project sử dụng các branch chính sau:

## main

- branch ổn định nhất
- chỉ chứa code đã kiểm thử và sẵn sàng release
- không code trực tiếp trên branch này

## develop

- branch tích hợp chính cho team
- tất cả feature branch merge vào đây trước
- dùng cho testing nội bộ

---

# 3. Feature Branches

Mỗi tính năng hoặc module mới phải được phát triển trên feature branch riêng.

Quy tắc đặt tên:

```text
feature/<module-name>
feature/<member>-<module>
feature/<short-description>
```

Ví dụ:

```text
feature/auth-login
feature/member2-product-crud
feature/comment-report
feature/review-moderation
```

---

# 4. Hotfix Branches

Nếu cần sửa lỗi gấp trên production:

```text
hotfix/<short-description>
```

Ví dụ:

```text
hotfix/fix-login-error
hotfix/fix-jwt-expiration
```

---

# 5. Release Branches (Optional)

Nếu cần chuẩn hóa cho release:

```text
release/<version>
```

Ví dụ:

```text
release/v1.0.0
```

Dùng khi team cần:

- đóng băng feature
- test trước khi merge vào `main`

---

# 6. Branch Ownership Suggestion

Theo phân công project:

- Member 1: auth, user, security
- Member 2: product, rating
- Member 3: category, post
- Member 4: review, comment, ai, analytics

Ví dụ branch theo module:

```text
feature/auth-user
feature/product-rating
feature/post-category
feature/review-comment-ai
```

Hoặc nhỏ hơn theo chức năng:

```text
feature/auth-register
feature/product-specs
feature/post-workflow
feature/comment-report
```

---

# 7. Merge Flow

Flow chuẩn:

1. Tạo branch từ `develop`
2. Code trên feature branch
3. Commit đầy đủ
4. Push lên remote
5. Tạo Pull Request vào `develop`
6. Review
7. Merge sau khi pass

---

# 8. Forbidden Actions

Không được:

- push trực tiếp vào `main`
- code trực tiếp trên `develop` nếu là feature lớn
- force push lên branch dùng chung nếu chưa thống nhất
- merge branch khi chưa test tối thiểu

---

# 9. Recommended Naming Convention

Quy tắc đặt tên nên ngắn, rõ và dễ hiểu.

Ví dụ tốt:

```text
feature/auth-login
feature/product-crud
feature/post-search
feature/comment-moderation
```

Ví dụ không tốt:

```text
feature/test
feature/code-moi
feature/fix1
```

---

# 10. Branch Lifecycle

Một feature branch nên có vòng đời:

Tạo branch

↓

Code

↓

Commit

↓

Push

↓

Create PR

↓

Review

↓

Merge vào develop

↓

Delete branch nếu đã xong

---

# 11. Summary

Chiến lược branch của project:

- `main`: ổn định, release-ready
- `develop`: tích hợp chính
- `feature/*`: phát triển tính năng
- `hotfix/*`: sửa lỗi gấp

Chiến lược này giúp:

- làm việc song song hiệu quả
- kiểm soát chất lượng code
- giảm xung đột khi merge