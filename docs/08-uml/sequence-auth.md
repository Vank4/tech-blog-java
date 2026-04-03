# Sequence Diagram - Authentication

## 1. Purpose

Tài liệu này mô tả **sequence flow của authentication** trong hệ thống Tech Blog Java.

Mục tiêu:

- mô tả luồng đăng nhập
- mô tả luồng xác thực JWT
- làm rõ vai trò của controller, service, security filter và database

---

# 2. Main Scenario: User Login

Actor tham gia:

- User
- AuthController
- AuthService
- UserRepository
- PasswordEncoder
- JwtTokenProvider

---

# 3. Login Sequence (Text)

```text
User
 → AuthController: POST /api/v1/auth/login
AuthController
 → AuthService: login(request)
AuthService
 → UserRepository: findByEmail(email)
UserRepository
 → AuthService: User
AuthService
 → PasswordEncoder: matches(rawPassword, hashedPassword)
PasswordEncoder
 → AuthService: true/false
AuthService
 → JwtTokenProvider: generateToken(user)
JwtTokenProvider
 → AuthService: JWT Token
AuthService
 → AuthController: LoginResponse
AuthController
 → User: ApiResponse<LoginResponse>
```

---

# 4. Detailed Steps

## Step 1

User gửi request đăng nhập với:

- email
- password

## Step 2

AuthController nhận request và validate input.

## Step 3

AuthController gọi `AuthService.login()`.

## Step 4

AuthService tìm user theo email từ database.

## Step 5

Nếu không tìm thấy user:

- ném exception phù hợp
- trả lỗi 401 hoặc 400 tùy policy

## Step 6

Nếu user tồn tại, AuthService dùng `PasswordEncoder.matches()` để kiểm tra mật khẩu.

## Step 7

Nếu mật khẩu đúng, AuthService tạo JWT token.

## Step 8

AuthController trả về:

- token
- thông tin user cơ bản
- roles

---

# 5. Login Failure Scenarios

## 5.1 User Not Found

```text
User
 → AuthController
AuthController
 → AuthService
AuthService
 → UserRepository: findByEmail(email)
UserRepository
 → AuthService: null
AuthService
 → GlobalExceptionHandler: Resource / Auth Exception
GlobalExceptionHandler
 → User: Error Response
```

## 5.2 Wrong Password

```text
User
 → AuthController
AuthController
 → AuthService
AuthService
 → UserRepository
UserRepository
 → AuthService: User
AuthService
 → PasswordEncoder: matches(...)
PasswordEncoder
 → AuthService: false
AuthService
 → GlobalExceptionHandler: Invalid Credentials
GlobalExceptionHandler
 → User: Error Response
```

---

# 6. JWT Request Validation Flow

Sau khi đăng nhập thành công, các request protected sẽ đi qua JWT filter.

Actor tham gia:

- Client
- JwtAuthenticationFilter
- JwtTokenProvider
- CustomUserDetailsService
- SecurityContext
- Controller

---

# 7. Protected Request Sequence

```text
Client
 → JwtAuthenticationFilter: HTTP Request with Authorization header
JwtAuthenticationFilter
 → JwtTokenProvider: parse/validate token
JwtTokenProvider
 → JwtAuthenticationFilter: token valid
JwtAuthenticationFilter
 → CustomUserDetailsService: loadUserByUsername(...)
CustomUserDetailsService
 → JwtAuthenticationFilter: UserDetails
JwtAuthenticationFilter
 → SecurityContext: setAuthentication(...)
JwtAuthenticationFilter
 → Controller: continue request
Controller
 → Client: ApiResponse
```

---

# 8. Invalid Token Flow

```text
Client
 → JwtAuthenticationFilter
JwtAuthenticationFilter
 → JwtTokenProvider: validate token
JwtTokenProvider
 → JwtAuthenticationFilter: invalid / expired
JwtAuthenticationFilter
 → AuthEntryPoint: unauthorized
AuthEntryPoint
 → Client: 401 Unauthorized
```

---

# 9. Authorization Flow

Sau khi authentication thành công, Spring Security tiếp tục kiểm tra quyền.

Ví dụ:

- API public: cho phép đi tiếp
- API user: yêu cầu authenticated
- API admin: yêu cầu role ADMIN

```text
Client
 → Security Filter Chain
 → Authentication success
 → Authorization rules
   ├── if role valid → Controller
   └── if role invalid → 403 Forbidden
```

---

# 10. Related Components

Các component chính tham gia vào auth flow:

- `AuthController`
- `AuthService`
- `UserRepository`
- `PasswordEncoder`
- `JwtTokenProvider`
- `JwtAuthenticationFilter`
- `CustomUserDetailsService`
- `AuthEntryPoint`
- `SecurityConfig`

---

# 11. Summary

Auth sequence của hệ thống gồm 2 luồng chính:

1. Login flow
2. JWT protected request flow

Flow này đảm bảo:

- user được xác thực bằng email/password
- mật khẩu được so sánh an toàn bằng BCrypt
- request protected được xác thực qua JWT
- phân quyền được kiểm tra bởi Spring Security