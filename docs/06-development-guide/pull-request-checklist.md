# Pull Request Checklist

## 1. Purpose

Checklist này được dùng trước khi tạo hoặc merge Pull Request trong dự án **Tech Blog Java**.

Mục tiêu:

- đảm bảo code đạt chất lượng tối thiểu
- tránh merge code lỗi
- giảm bug khi tích hợp
- chuẩn hóa quy trình review

---

# 2. Before Creating PR

Trước khi tạo Pull Request, developer phải tự kiểm tra:

- [ ] Đã pull `develop` mới nhất
- [ ] Branch đang đúng mục đích
- [ ] Code build thành công
- [ ] Không có file thừa không cần commit
- [ ] Không commit `target/`, `logs/`, file local config nhạy cảm
- [ ] Đã test thủ công các API liên quan
- [ ] Không phá module khác

---

# 3. Code Quality Checklist

- [ ] Tên class, method, variable đúng convention
- [ ] Không viết business logic trong controller
- [ ] Có dùng DTO thay vì trả entity trực tiếp
- [ ] Có xử lý exception phù hợp
- [ ] Có dùng `ApiResponse` chuẩn
- [ ] Có validation cho request input
- [ ] Không hardcode giá trị trạng thái nếu đã có enum

---

# 4. Architecture Checklist

- [ ] Code đặt đúng package
- [ ] Module đúng ownership
- [ ] Không phụ thuộc sai vào module khác
- [ ] Không phá chuẩn domain-based structure
- [ ] Không thêm dependency mới nếu chưa cần

---

# 5. API Checklist

Nếu PR liên quan đến API:

- [ ] Endpoint đặt đúng naming convention
- [ ] HTTP method dùng đúng
- [ ] Response format đúng chuẩn
- [ ] Error handling đúng chuẩn
- [ ] Security rule đúng (public / protected / admin)
- [ ] Swagger có thể test được

---

# 6. Database Checklist

Nếu PR liên quan đến database:

- [ ] Entity mapping đúng
- [ ] Quan hệ JPA đúng
- [ ] Không tạo query gây lỗi hiệu năng nghiêm trọng
- [ ] Có update migration hoặc docs liên quan nếu cần
- [ ] Không làm vỡ dữ liệu cũ

---

# 7. Testing Checklist

- [ ] Project chạy được trên local
- [ ] API chính đã test
- [ ] Không có lỗi startup
- [ ] Không có lỗi obvious trong log
- [ ] Nếu có test thì test pass

---

# 8. Documentation Checklist

Nếu PR thay đổi API / database / architecture:

- [ ] Đã cập nhật docs tương ứng
- [ ] Đã cập nhật API docs nếu endpoint thay đổi
- [ ] Đã cập nhật database docs nếu bảng/cột thay đổi

---

# 9. PR Description Template

Mỗi Pull Request nên có nội dung:

## Summary
Mô tả ngắn thay đổi.

## Changes
- liệt kê những gì đã làm

## Testing
- mô tả đã test gì

## Notes
- lưu ý cho reviewer nếu có

Ví dụ:

```text
Summary:
Implement product CRUD API.

Changes:
- add ProductController
- add ProductService
- add ProductRepository
- add CreateProductRequest and ProductResponse

Testing:
- tested create product API on local
- verified swagger works
```

---

# 10. Reviewer Checklist

Reviewer nên kiểm tra:

- [ ] Code có đúng mục tiêu PR không
- [ ] Logic nghiệp vụ có hợp lý không
- [ ] Naming và package đúng convention
- [ ] Security có đúng không
- [ ] Response / exception có đúng chuẩn không
- [ ] PR có quá lớn không
- [ ] Có ảnh hưởng module khác không

---

# 11. Merge Conditions

PR chỉ nên được merge khi:

- [ ] Build pass
- [ ] Không có lỗi rõ ràng
- [ ] Reviewer đồng ý
- [ ] Đã xử lý feedback chính
- [ ] Không conflict với develop

---

# 12. Summary

Checklist PR giúp đảm bảo:

- code chất lượng ổn định
- merge an toàn
- giảm bug khi tích hợp
- team làm việc thống nhất hơn