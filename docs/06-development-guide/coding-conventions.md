# Coding Conventions

## 1. Purpose

Tài liệu này mô tả các quy ước code cho dự án **Tech Blog Java Backend**.

Mục tiêu:

- đảm bảo code nhất quán
- giúp code dễ đọc, dễ maintain
- giảm lỗi do khác biệt phong cách code
- giúp reviewer dễ review hơn

---

# 2. General Principles

Các nguyên tắc chung:

1. Viết code rõ ràng, dễ hiểu.
2. Ưu tiên tính nhất quán hơn sở thích cá nhân.
3. Không viết business logic trong controller.
4. Không trả entity trực tiếp ra API.
5. Dùng DTO cho request và response.

---

# 3. Naming Conventions

## Package Names

- dùng lowercase
- không dùng ký tự đặc biệt
- đặt theo domain hoặc chức năng

Ví dụ:

```text
com.techblog.domain.product
com.techblog.security
com.techblog.common.exception
```

## Class Names

- dùng PascalCase

Ví dụ:

```text
ProductService
UserController
JwtTokenProvider
```

## Method Names

- dùng camelCase
- bắt đầu bằng động từ

Ví dụ:

```text
getProductById
createUser
updateReview
deleteComment
```

## Variable Names

- dùng camelCase
- tên có ý nghĩa rõ ràng

Ví dụ:

```text
productId
currentUser
averageRating
commentStatus
```

## Constant Names

- dùng UPPER_SNAKE_CASE

Ví dụ:

```text
DEFAULT_PAGE_SIZE
JWT_EXPIRATION_TIME
MAX_RETRY_COUNT
```

---

# 4. Package Structure Rules

Project sử dụng **domain-based package structure**.

Mỗi module nên có các package:

```text
controller
service
repository
model
dto
mapper
```

Ví dụ:

```text
domain/product
├── controller
├── service
├── repository
├── model
├── dto
└── mapper
```

---

# 5. Controller Rules

Controller chỉ nên làm:

- nhận request
- validate input cơ bản
- gọi service
- trả response

Controller không nên:

- viết business logic
- query database trực tiếp
- xử lý logic phức tạp

Ví dụ tốt:

```java
@GetMapping("/{id}")
public ApiResponse<ProductResponse> getProduct(@PathVariable Long id) {
    return ApiResponse.success("Product retrieved successfully", productService.getProductById(id));
}
```

---

# 6. Service Rules

Service là nơi chứa business logic.

Service nên:

- xử lý nghiệp vụ
- gọi repository
- kiểm tra điều kiện business
- ném exception phù hợp

Service không nên:

- xử lý HTTP-specific logic
- trả entity thô cho client
- chứa query SQL trực tiếp nếu không cần

---

# 7. Repository Rules

Repository chịu trách nhiệm truy cập dữ liệu.

Repository nên:

- extend `JpaRepository`
- viết query rõ ràng
- chỉ tập trung vào persistence

Repository không nên:

- chứa business logic
- phụ thuộc vào controller

---

# 8. DTO Rules

Phải dùng DTO cho request và response.

## Request DTO

Dùng cho input từ client.

Ví dụ:

```text
CreateProductRequest
LoginRequest
UpdateProfileRequest
```

## Response DTO

Dùng cho output ra client.

Ví dụ:

```text
ProductResponse
UserResponse
ReviewResponse
```

Không nên:

- trả entity trực tiếp
- dùng cùng một DTO cho nhiều context rất khác nhau nếu dễ gây rối

---

# 9. Entity Rules

Entity đại diện cho bảng database.

Quy tắc:

- entity chỉ chứa dữ liệu và mapping
- không nhồi business logic phức tạp vào entity
- dùng annotation JPA đúng chuẩn
- thống nhất naming field với database mapping

Ví dụ:

```java
@Entity
@Table(name = "products")
public class Product {
}
```

---

# 10. Enum Usage

Các giá trị trạng thái phải dùng enum thay vì hardcode string.

Ví dụ:

- `UserStatus`
- `PostStatus`
- `ReviewStatus`
- `CommentStatus`

Không nên:

```java
if (status.equals("ACTIVE")) { ... }
```

Nên:

```java
if (status == UserStatus.ACTIVE) { ... }
```

---

# 11. Exception Handling Rules

- tất cả exception nên đi qua `GlobalExceptionHandler`
- dùng custom exception rõ nghĩa
- không `catch Exception` bừa bãi
- không trả stacktrace ra client

Ví dụ custom exception:

- `ResourceNotFoundException`
- `BadRequestException`
- `ConflictException`
- `ForbiddenException`

---

# 12. Response Rules

Tất cả API phải dùng chuẩn `ApiResponse`.

Ví dụ:

```json
{
  "success": true,
  "message": "Request successful",
  "data": {}
}
```

Không trả dữ liệu raw trực tiếp nếu không thật sự cần.

---

# 13. Validation Rules

Dùng validation annotation cho request DTO.

Ví dụ:

- `@NotBlank`
- `@Email`
- `@Size`
- `@NotNull`
- `@Min`
- `@Max`

Validation message phải rõ ràng.

---

# 14. Formatting Rules

Khuyến nghị:

- dùng format mặc định của IntelliJ
- indent 4 spaces
- line length vừa phải
- không viết method quá dài
- tách logic rõ ràng

Một method nên:

- làm một việc chính
- dễ đọc
- dễ test

---

# 15. Commenting Rules

Chỉ comment khi cần.

Nên comment khi:

- logic khó hiểu
- quyết định kỹ thuật đặc biệt
- workaround tạm thời

Không nên comment các dòng quá hiển nhiên.

---

# 16. Logging Rules

Dùng logging cho:

- startup quan trọng
- lỗi hệ thống
- auth failure
- xử lý AI
- moderation events quan trọng

Không log:

- password
- JWT secret
- dữ liệu nhạy cảm

---

# 17. Git Commit Message Rules

Commit message nên ngắn, rõ ràng.

Ví dụ tốt:

```text
feat: add product CRUD API
fix: handle invalid JWT token
refactor: extract review mapping logic
docs: update database overview
```

---

# 18. Summary

Coding conventions của project tập trung vào:

- tính nhất quán
- tính rõ ràng
- tách lớp đúng vai trò
- chuẩn hóa API và exception

Tuân thủ tài liệu này giúp:

- code dễ review
- dễ maintain
- dễ onboard thành viên mới