# Non Functional Requirements

Các yêu cầu phi chức năng của hệ thống.

---

# 1. Performance

- API response time < 500ms trong điều kiện bình thường
- Hệ thống phải hỗ trợ ít nhất 100 request/second
- Query database phải được tối ưu bằng index

---

# 2. Scalability

Hệ thống phải có khả năng mở rộng:

- tách service
- scale database
- cache dữ liệu

---

# 3. Security

Yêu cầu bảo mật:

- Password phải được hash
- API phải xác thực bằng JWT
- Role-based access control
- Chống SQL Injection
- Chống XSS

---

# 4. Reliability

- Hệ thống phải đảm bảo uptime cao
- Các lỗi phải được log

---

# 5. Maintainability

Code phải:

- tuân theo coding conventions
- có cấu trúc module rõ ràng
- dễ mở rộng

---

# 6. Logging

Hệ thống phải log:

- request
- exception
- authentication failure

---

# 7. Documentation

Hệ thống phải có:

- API documentation
- Database documentation
- Architecture documentation

---

# 8. Deployment

Hệ thống có thể deploy:

- Docker
- Cloud server