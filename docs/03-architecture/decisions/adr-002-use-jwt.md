# ADR-002: Use JWT for Authentication

## Status

Accepted

---

## Context

Hệ thống cần cơ chế xác thực người dùng để bảo vệ API.

Các yêu cầu:

- API phải được xác thực
- Hỗ trợ stateless authentication
- Phù hợp với REST API
- dễ tích hợp với frontend

Các phương án được xem xét:

- Session-based authentication
- OAuth2
- JWT (JSON Web Token)

---

## Decision

Hệ thống quyết định sử dụng **JWT (JSON Web Token)** để xác thực người dùng.

---

## Reasoning

JWT được chọn vì các lý do:

1. Stateless authentication.
2. Không cần lưu session trên server.
3. Phù hợp với REST API.
4. Hiệu năng tốt.
5. Dễ tích hợp với frontend.

Luồng xác thực:

User login  
↓  
Server tạo JWT token  
↓  
Client lưu token  
↓  
Client gửi token trong header  
↓  
Server validate token

Header format:

```
Authorization: Bearer <token>
```

---

## Consequences

### Positive

- scalable
- không cần session storage
- phù hợp cho microservices

### Negative

- token không thể revoke dễ dàng
- cần thêm refresh token nếu cần

---

## Alternatives Considered

### Session Authentication

Ưu điểm:

- đơn giản
- dễ revoke session

Nhược điểm:

- không stateless
- khó scale

---

### OAuth2

Ưu điểm:

- bảo mật cao
- chuẩn industry

Nhược điểm:

- phức tạp
- overkill cho project hiện tại

---

## Conclusion

JWT là lựa chọn phù hợp nhất cho hệ thống backend REST API của project.