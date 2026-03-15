# Handoff Checklist

## 1. Purpose

Checklist này dùng cho **nhóm trưởng** trước khi bàn giao khung hệ thống cho các thành viên khác bắt đầu code.

Mục tiêu:

- đảm bảo project skeleton đã sẵn sàng
- đảm bảo thành viên khác có thể checkout và bắt tay vào code ngay
- tránh việc mỗi người phải tự dựng lại khung riêng

---

# 2. Project Foundation Checklist

- [ ] Repository đã được tạo
- [ ] Cấu trúc thư mục project đã hoàn chỉnh
- [ ] Package structure đã được thống nhất
- [ ] Main application chạy được
- [ ] Maven build được
- [ ] `.gitignore` đã đúng
- [ ] Không commit `target/`, `logs/`, file rác

---

# 3. Database Checklist

- [ ] Database schema đã được chốt
- [ ] Kết nối MySQL hoạt động
- [ ] Các bảng nền đã có hoặc có thể tự tạo
- [ ] Entity mapping cơ bản đã đúng
- [ ] Seed data đã hoạt động
- [ ] Có tài liệu database overview
- [ ] Có danh sách bảng và constraints

---

# 4. Security Checklist

- [ ] JWT login flow hoạt động
- [ ] Password hashing hoạt động
- [ ] Security filter hoạt động
- [ ] Có phân quyền role cơ bản
- [ ] Public và protected endpoints đã phân tách
- [ ] Có tài liệu security architecture

---

# 5. API Foundation Checklist

- [ ] Response format chuẩn đã có
- [ ] Global exception handler đã có
- [ ] Swagger chạy được
- [ ] API naming conventions đã chốt
- [ ] Có docs cho các module API chính

---

# 6. Shared Code Checklist

- [ ] Base audit entity đã có
- [ ] Enum hệ thống đã có
- [ ] Common exception classes đã có
- [ ] ApiResponse đã có
- [ ] Validation structure đã có
- [ ] Utility classes cơ bản đã có

---

# 7. Config Checklist

- [ ] `application.yml` đã có
- [ ] `application-dev.yml` đã chạy được
- [ ] `application-prod.yml` đã có khung
- [ ] Có mô tả environment config
- [ ] JWT secret đã cấu hình
- [ ] CORS config đã có
- [ ] Log config đã có

---

# 8. Team Handoff Checklist

- [ ] Đã có tài liệu team ownership
- [ ] Đã có branch strategy
- [ ] Đã có coding conventions
- [ ] Đã có git workflow
- [ ] Đã có PR checklist
- [ ] Mỗi thành viên biết module mình phụ trách

---

# 9. Ready-to-Code Checklist

Trước khi giao việc, cần đảm bảo thành viên khác có thể:

- [ ] clone repo
- [ ] cấu hình local database
- [ ] chạy được project
- [ ] mở được swagger
- [ ] test được ít nhất 1 API
- [ ] tạo branch feature riêng
- [ ] bắt đầu code trong module của mình

---

# 10. Minimum Demo Checklist

Nhóm trưởng nên đảm bảo tối thiểu có thể demo:

- [ ] app start thành công
- [ ] login API hoạt động
- [ ] 1 API protected hoạt động
- [ ] 1 module CRUD mẫu hoạt động
- [ ] database kết nối ổn định

---

# 11. Notes for Team Lead

Nhóm trưởng nên hoàn thành trước:

- skeleton project
- config nền
- auth nền
- docs nền
- DB nền
- workflow nền

Không nên để thành viên khác tự quyết:

- cấu trúc package
- response format
- exception format
- branch strategy
- coding conventions

---

# 12. Summary

Checklist bàn giao giúp đảm bảo:

- project có nền tảng ổn định
- team không bị lệch kiến trúc
- developer mới vào việc nhanh
- giảm thời gian setup và hỏi lại