# AI Module

## 1. Purpose

AI Module chịu trách nhiệm cho **phân tích cảm xúc (sentiment analysis)** của comment trong hệ thống Tech Blog Java.

Đây là module tạo ra giá trị khác biệt của sản phẩm, giúp tổng hợp phản hồi cộng đồng theo hướng dữ liệu.

---

# 2. Responsibilities

AI Module chịu trách nhiệm:

- gọi AI service để phân tích comment
- lưu sentiment result
- lấy sentiment theo comment
- tổng hợp sentiment theo product
- hỗ trợ manual labeling
- quản lý AI settings
- cung cấp dữ liệu cho AI dashboard

---

# 3. Main Features

## 3.1 Analyze Comment

Phân tích sentiment cho comment.

## 3.2 Get Comment Sentiment

Xem nhãn sentiment của comment.

## 3.3 Product Sentiment Summary

Tổng hợp số lượng positive / neutral / negative theo product.

## 3.4 Manual Labeling

Admin chỉnh nhãn sentiment thủ công khi cần.

## 3.5 AI Settings

Cấu hình:

- bật/tắt AI
- confidence threshold
- auto analyze
- minimum comments for summary

---

# 4. Main Packages

```text
domain/ai
├── controller
├── dto
├── model
├── repository
└── service
```

Infrastructure liên quan:

```text
infrastructure/ai_client
```

---

# 5. Main APIs

- `POST /api/v1/ai/analyze-comment/{commentId}`
- `GET /api/v1/ai/comments/{commentId}/sentiment`
- `GET /api/v1/ai/products/{productId}/summary`
- `POST /api/v1/ai/products/{productId}/recalculate`
- `PATCH /api/v1/ai/comments/{commentId}/label`
- `GET /api/v1/ai/settings`
- `PUT /api/v1/ai/settings`
- `GET /api/v1/ai/analytics`

---

# 6. Main Data Involved

Các bảng liên quan:

- ai_analysis_results
- có thể có ai_settings table nếu triển khai persistence riêng

Nguồn dữ liệu đầu vào:

- comments
- products

---

# 7. Business Rules

- chỉ phân tích comment hợp lệ
- sentiment label hợp lệ gồm:
    - POSITIVE
    - NEUTRAL
    - NEGATIVE
- product summary chỉ có ý nghĩa khi đủ số lượng comment tối thiểu
- admin có thể override nhãn bằng manual labeling
- AI settings chỉ admin được chỉnh

---

# 8. Dependencies

AI Module phụ thuộc vào:

- Comment Module
- Product Module
- Infrastructure AI client
- Admin permission

Analytics Module có thể dùng dữ liệu từ AI Module.

---

# 9. Suggested Service Methods

- `analyzeComment(Long commentId)`
- `getCommentSentiment(Long commentId)`
- `getProductSentimentSummary(Long productId)`
- `recalculateProductSummary(Long productId)`
- `labelCommentSentiment(Long commentId, ManualLabelRequest request)`
- `getAiSettings()`
- `updateAiSettings(UpdateAiSettingsRequest request)`
- `getAiAnalytics()`

---

# 10. Validation Rules

- commentId phải tồn tại
- sentimentLabel manual phải hợp lệ
- threshold phải nằm trong khoảng hợp lệ
- minimumCommentsForSummary >= 1

---

# 11. Error Cases

- comment không tồn tại
- AI service unavailable
- kết quả AI không parse được
- settings không hợp lệ
- user không đủ quyền admin

---

# 12. Development Notes

- nên tách AI client ra infrastructure layer
- raw result nên lưu nếu hữu ích cho debugging
- nếu muốn tránh tăng độ trễ request comment, có thể chuyển sang async sau
- cần thống nhất quy tắc khi nào trigger AI: khi create comment hay khi approve comment

---

# 13. Definition of Done

AI Module được xem là hoàn thành khi:

- phân tích sentiment cho comment hoạt động
- lưu kết quả được
- xem sentiment theo comment được
- summary theo product được
- manual labeling được
- settings cập nhật được
- admin dashboard data cơ bản có thể lấy được