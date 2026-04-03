# Analytics Module

## 1. Purpose

Analytics Module chịu trách nhiệm cung cấp **dữ liệu thống kê và dashboard tổng quan** cho hệ thống Tech Blog Java.

Module này giúp admin theo dõi hoạt động của nền tảng và hỗ trợ quyết định vận hành.

---

# 2. Responsibilities

Analytics Module chịu trách nhiệm:

- thống kê user
- thống kê sản phẩm
- thống kê bài viết
- thống kê review
- thống kê comment
- thống kê moderation queues
- tổng hợp dữ liệu AI sentiment
- cung cấp dữ liệu dashboard admin

---

# 3. Main Features

## 3.1 Dashboard Summary

Hiển thị số liệu tổng quan:

- total users
- total products
- total posts
- total reviews
- total comments

## 3.2 Pending Moderation Counts

Thống kê số lượng:

- pending posts
- pending reviews
- pending comments
- open reports

## 3.3 AI Analytics

Tổng hợp sentiment:

- positive count
- neutral count
- negative count
- total analyzed comments

## 3.4 View / Event Analytics

Nếu hệ thống lưu events, có thể thống kê:

- most viewed posts
- most viewed products
- user activity trends

---

# 4. Main Packages

```text
domain/analytics
├── controller
├── dto
├── model
├── repository
└── service
```

---

# 5. Main APIs

Tùy thiết kế, analytics có thể xuất hiện tại:

- `GET /api/v1/admin/dashboard`
- `GET /api/v1/admin/analytics/summary`
- `GET /api/v1/ai/analytics`

Hoặc có thể gom thành controller riêng trong module analytics.

---

# 6. Main Data Involved

Nguồn dữ liệu tổng hợp từ:

- users
- products
- posts
- reviews
- comments
- comment_reports
- ai_analysis_results
- analytics_events (nếu có)

---

# 7. Business Rules

- analytics chủ yếu dành cho admin
- dashboard phải trả dữ liệu nhanh và dễ đọc
- số liệu thống kê cần rõ ràng và nhất quán
- AI analytics phải đồng bộ với data sentiment thực tế
- moderation counts phải phản ánh đúng trạng thái hiện tại

---

# 8. Dependencies

Analytics Module phụ thuộc vào nhiều module khác:

- User Module
- Product Module
- Post Module
- Review Module
- Comment Module
- AI Module

Đây là module tổng hợp, không nên để các module khác phụ thuộc ngược vào nó.

---

# 9. Suggested Service Methods

- `getDashboardSummary()`
- `getAnalyticsSummary()`
- `getPendingModerationCounts()`
- `getAiAnalyticsSummary()`
- `getTopViewedPosts()`
- `getTopViewedProducts()`

---

# 10. Validation Rules

- nếu có query range hoặc filter thời gian thì phải validate start/end dates
- chỉ admin mới truy cập dashboard analytics
- response phải theo format dễ dùng cho frontend dashboard

---

# 11. Error Cases

- user không đủ quyền admin
- dữ liệu tổng hợp không khả dụng
- nguồn dữ liệu analytics chưa được seed hoặc chưa đủ

---

# 12. Development Notes

- ưu tiên query đơn giản và index tốt cho dashboard
- nếu số lượng dữ liệu lớn, có thể cân nhắc pre-aggregate sau
- analytics nên tách rõ với business CRUD để code dễ maintain
- có thể bắt đầu từ dashboard summary cơ bản, sau đó mở rộng

---

# 13. Definition of Done

Analytics Module được xem là hoàn thành khi:

- admin dashboard summary hoạt động
- moderation counts hoạt động
- AI analytics summary hoạt động
- response đúng chuẩn
- role admin được kiểm soát đúng