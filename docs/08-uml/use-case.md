# Use Case Diagram Description

## 1. Purpose

Tài liệu này mô tả các **use case chính** của hệ thống **Tech Blog Java** theo góc nhìn actor và chức năng.

Mục tiêu:

- xác định actor của hệ thống
- mô tả các hành vi chính mà actor có thể thực hiện
- làm cơ sở cho thiết kế API, module và quyền truy cập

Use case trong tài liệu này được mô tả theo dạng text để dễ đọc và dễ cập nhật cùng source code.

---

# 2. Main Actors

Hệ thống có 4 actor chính:

## 2.1 Guest

Người dùng chưa đăng nhập.

Guest có thể:

- xem danh sách sản phẩm
- xem chi tiết sản phẩm
- xem danh sách bài viết
- xem chi tiết bài viết
- xem danh sách review
- xem comment public

---

## 2.2 User

Người dùng đã đăng nhập với role `USER`.

User có thể:

- đăng nhập
- cập nhật profile
- đổi mật khẩu
- chấm rating sản phẩm
- thêm sản phẩm vào compare
- comment
- reply comment
- report comment
- xem danh sách yêu thích / compare của mình

---

## 2.3 Author

Người dùng có role `AUTHOR`.

Author có thể:

- thực hiện tất cả các chức năng của User
- tạo bài viết
- cập nhật bài viết của mình
- submit bài viết để duyệt
- tạo review sản phẩm
- cập nhật review của mình
- submit review để duyệt

---

## 2.4 Admin

Người dùng có role `ADMIN`.

Admin có thể:

- thực hiện chức năng quản trị user
- quản lý category
- quản lý product
- approve / reject / hide post
- approve / reject / hide review
- approve / hide comment
- xử lý comment report
- quản lý AI settings
- xem analytics dashboard
- quản lý featured content

---

# 3. Main Use Cases by Module

---

## 3.1 Authentication Use Cases

### Guest
- Register
- Login
- Verify Email
- Forgot Password
- Reset Password

### User / Author / Admin
- Logout

---

## 3.2 User Management Use Cases

### User
- View own profile
- Update own profile
- Change password

### Admin
- View user list
- View user detail
- Assign role
- Ban user
- Unban user

---

## 3.3 Category Use Cases

### Guest / User / Author
- View category list
- View category detail

### Admin
- Create category
- Update category
- Disable category
- Enable category
- Delete category

---

## 3.4 Product Use Cases

### Guest / User / Author
- View product list
- View product detail
- Filter products

### User / Author
- Rate product
- Add product to compare
- Remove product from compare

### Admin
- Create product
- Update product
- Publish product
- Hide product
- Delete product
- Add product image
- Remove product image
- Add product spec
- Update product spec
- Delete product spec

---

## 3.5 Post Use Cases

### Guest / User / Author
- View post list
- View post detail
- Search posts

### Author
- Create post
- Update own post
- Submit post for moderation
- Delete own post

### Admin
- Approve post
- Reject post
- Hide post
- View moderation logs
- Set featured post

---

## 3.6 Review Use Cases

### Guest / User / Author
- View review list
- View review detail
- View reviews by product

### Author
- Create review
- Update own review
- Submit review for moderation
- Delete own review

### Admin
- Approve review
- Reject review
- Hide review
- View review moderation logs

---

## 3.7 Comment Use Cases

### Guest
- View comments

### User / Author / Admin
- Create comment
- Reply comment
- Update own comment
- Delete own comment
- Report comment

### Admin
- Approve comment
- Hide comment
- View comment reports
- Resolve comment report
- View comment moderation logs

---

## 3.8 AI Use Cases

### Admin
- Analyze comment sentiment manually
- View sentiment by comment
- Recalculate product sentiment summary
- Manual label sentiment
- View AI settings
- Update AI settings
- View AI analytics

### Guest / User / Author
- View public product sentiment summary (nếu hệ thống public)

---

## 3.9 Analytics Use Cases

### Admin
- View admin dashboard
- View analytics summary
- View moderation counts
- View AI analytics summary

---

# 4. Textual Use Case Diagram

```text
Guest
 ├── Register
 ├── Login
 ├── View Product List
 ├── View Product Detail
 ├── View Post List
 ├── View Post Detail
 ├── View Review List
 └── View Comments

User
 ├── View / Update Profile
 ├── Change Password
 ├── Rate Product
 ├── Add To Compare
 ├── Create Comment
 ├── Reply Comment
 ├── Update Own Comment
 ├── Delete Own Comment
 └── Report Comment

Author
 ├── All User Use Cases
 ├── Create Post
 ├── Update Own Post
 ├── Submit Post
 ├── Create Review
 ├── Update Own Review
 └── Submit Review

Admin
 ├── Manage Users
 ├── Manage Categories
 ├── Manage Products
 ├── Approve/Reject/Hide Post
 ├── Approve/Reject/Hide Review
 ├── Approve/Hide Comment
 ├── Resolve Comment Reports
 ├── Manage AI Settings
 ├── View Analytics Dashboard
 └── Manage Featured Content
```

---

# 5. High Priority Use Cases

Các use case quan trọng nhất của hệ thống:

1. User login
2. Admin create/manage product
3. Author create/submit post
4. Author create/submit review
5. User comment and report comment
6. Admin moderate content
7. AI analyze comment sentiment
8. Admin view dashboard

---

# 6. Summary

Use case của Tech Blog Java xoay quanh 4 actor chính:

- Guest
- User
- Author
- Admin

Các use case được phân nhóm theo module:

- Authentication
- User Management
- Category
- Product
- Post
- Review
- Comment
- AI
- Analytics

Tài liệu này là nền tảng để:

- thiết kế API
- thiết kế quyền truy cập
- phân chia module
- xây dựng sequence diagram và state machine