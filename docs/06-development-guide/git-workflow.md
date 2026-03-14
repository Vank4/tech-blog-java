# Git Workflow

## 1. Purpose

Tài liệu này mô tả quy trình làm việc với Git cho dự án **Tech Blog Java**.

Mục tiêu:

- chuẩn hóa cách commit, push, merge
- giảm conflict
- giúp team làm việc song song hiệu quả
- giữ lịch sử source code sạch và dễ theo dõi

---

# 2. Workflow Overview

Quy trình tiêu chuẩn:

1. Pull branch `develop` mới nhất
2. Tạo feature branch mới
3. Code trên feature branch
4. Commit theo từng phần rõ ràng
5. Push branch lên remote
6. Tạo Pull Request vào `develop`
7. Review
8. Merge khi pass

---

# 3. Step-by-Step Workflow

## Step 1: Update develop

```bash
git checkout develop
git pull origin develop
```

## Step 2: Create feature branch

```bash
git checkout -b feature/product-crud
```

## Step 3: Code and test locally

- code chức năng
- chạy project
- test API liên quan
- kiểm tra không phá module khác

## Step 4: Commit changes

```bash
git add .
git commit -m "feat: add product CRUD API"
```

## Step 5: Push branch

```bash
git push origin feature/product-crud
```

## Step 6: Create Pull Request

Tạo PR từ:

```text
feature/product-crud -> develop
```

## Step 7: Review and merge

- reviewer kiểm tra code
- sửa nếu có feedback
- merge sau khi pass

---

# 4. Commit Guidelines

Mỗi commit nên:

- có scope rõ ràng
- không quá lớn
- mô tả đúng thay đổi

Ví dụ commit type:

- `feat`
- `fix`
- `refactor`
- `docs`
- `test`
- `chore`

Ví dụ:

```text
feat: implement login API
fix: handle null category id
docs: update auth API document
test: add product service test
```

---

# 5. Pull Before Push

Trước khi push hoặc tạo PR, nên đồng bộ `develop` mới nhất.

Ví dụ:

```bash
git checkout develop
git pull origin develop
git checkout feature/product-crud
git rebase develop
```

Hoặc merge `develop` vào branch feature nếu team dùng merge strategy.

---

# 6. Conflict Resolution

Khi có conflict:

1. đọc kỹ file conflict
2. ưu tiên logic mới nhất đúng nghiệp vụ
3. kiểm tra không ghi đè code người khác sai
4. chạy lại project sau khi resolve

Sau khi resolve:

```bash
git add .
git commit -m "fix: resolve merge conflict with develop"
```

---

# 7. Do and Don't

## Nên làm

- commit nhỏ, rõ ràng
- push thường xuyên
- tạo PR sớm
- cập nhật branch thường xuyên từ develop

## Không nên

- commit toàn bộ project bừa bãi
- push code chưa build được
- merge khi chưa test tối thiểu
- sửa module người khác không thông báo

---

# 8. PR Lifecycle

Một Pull Request nên có vòng đời:

Tạo PR

↓

Reviewer đọc code

↓

Feedback

↓

Developer sửa

↓

Re-review

↓

Merge

---

# 9. Suggested Review Focus

Khi review PR nên xem:

- code có đúng module không
- business logic có đúng không
- response format có đúng chuẩn không
- exception handling có đúng không
- naming có đúng conventions không
- có phá API cũ không

---

# 10. Branch Cleanup

Sau khi merge xong:

- xóa feature branch local
- xóa feature branch remote nếu không dùng nữa

Ví dụ:

```bash
git branch -d feature/product-crud
git push origin --delete feature/product-crud
```

---

# 11. Summary

Git workflow chuẩn của project:

- làm việc trên `feature/*`
- merge vào `develop`
- không push trực tiếp vào `main`
- commit rõ ràng
- review trước khi merge

Workflow này giúp:

- codebase sạch
- giảm xung đột
- kiểm soát chất lượng tốt hơn