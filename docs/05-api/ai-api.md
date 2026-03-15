# AI API

## 1. Purpose

Tài liệu này mô tả các API liên quan đến **AI sentiment analysis** trong hệ thống **Tech Blog Java Backend**.

Các chức năng chính:

- phân tích sentiment cho comment
- lấy kết quả sentiment theo comment
- tổng hợp sentiment theo product
- quản lý manual labeling
- cập nhật AI settings

Base path:

```
/api/v1/ai
```

---

# 2. Analyze Comment Sentiment

## Endpoint

```
POST /api/v1/ai/analyze-comment/{commentId}
```

## Description

Kích hoạt phân tích sentiment cho một comment.

## Authentication

Yêu cầu JWT và role ADMIN.

## Path Variables

| Name | Type | Required | Description |
|------|------|----------|-------------|
| commentId | long | yes | ID của comment |

## Success Response

```json
{
  "success": true,
  "message": "Comment sentiment analysis completed successfully",
  "data": {
    "commentId": 10,
    "sentimentLabel": "POSITIVE",
    "confidenceScore": 0.94
  }
}
```

## Notes

- API này có thể dùng cho manual re-run
- Trong luồng thực tế, hệ thống có thể auto gọi AI sau khi comment được tạo

---

# 3. Get Sentiment by Comment

## Endpoint

```
GET /api/v1/ai/comments/{commentId}/sentiment
```

## Description

Lấy kết quả sentiment của một comment.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Comment sentiment retrieved successfully",
  "data": {
    "commentId": 10,
    "sentimentLabel": "NEGATIVE",
    "confidenceScore": 0.87,
    "rawResult": "negative",
    "createdAt": "2026-03-14T11:00:00"
  }
}
```

---

# 4. Get Product Sentiment Summary

## Endpoint

```
GET /api/v1/ai/products/{productId}/summary
```

## Description

Lấy thống kê sentiment tổng hợp của sản phẩm dựa trên comment.

## Authentication

Không yêu cầu hoặc yêu cầu JWT tùy policy triển khai.  
Khuyến nghị: cho phép public read-only.

## Success Response

```json
{
  "success": true,
  "message": "Product sentiment summary retrieved successfully",
  "data": {
    "productId": 1,
    "positiveCount": 70,
    "neutralCount": 20,
    "negativeCount": 10,
    "totalAnalyzedComments": 100,
    "conclusion": "GOOD"
  }
}
```

---

# 5. Recalculate Product Sentiment Summary

## Endpoint

```
POST /api/v1/ai/products/{productId}/recalculate
```

## Description

Tính toán lại sentiment summary của sản phẩm.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "Product sentiment summary recalculated successfully",
  "data": {
    "productId": 1,
    "conclusion": "GOOD"
  }
}
```

---

# 6. Manual Label Comment Sentiment

## Endpoint

```
PATCH /api/v1/ai/comments/{commentId}/label
```

## Description

Admin gán nhãn sentiment thủ công cho comment.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "sentimentLabel": "NEUTRAL",
  "note": "Manual correction by admin"
}
```

## Validation Rules

- `sentimentLabel` bắt buộc
- giá trị hợp lệ: `POSITIVE`, `NEUTRAL`, `NEGATIVE`

## Success Response

```json
{
  "success": true,
  "message": "Comment sentiment labeled successfully",
  "data": {
    "commentId": 10,
    "sentimentLabel": "NEUTRAL"
  }
}
```

---

# 7. Get AI Settings

## Endpoint

```
GET /api/v1/ai/settings
```

## Description

Lấy cấu hình hiện tại của AI module.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "AI settings retrieved successfully",
  "data": {
    "enabled": true,
    "confidenceThreshold": 0.7,
    "autoAnalyzeNewComments": true,
    "minimumCommentsForSummary": 5
  }
}
```

---

# 8. Update AI Settings

## Endpoint

```
PUT /api/v1/ai/settings
```

## Description

Cập nhật cấu hình AI module.

## Authentication

Yêu cầu JWT và role ADMIN.

## Request Body

```json
{
  "enabled": true,
  "confidenceThreshold": 0.75,
  "autoAnalyzeNewComments": true,
  "minimumCommentsForSummary": 10
}
```

## Success Response

```json
{
  "success": true,
  "message": "AI settings updated successfully",
  "data": {
    "enabled": true,
    "confidenceThreshold": 0.75,
    "autoAnalyzeNewComments": true,
    "minimumCommentsForSummary": 10
  }
}
```

---

# 9. Get AI Analytics Dashboard Data

## Endpoint

```
GET /api/v1/ai/analytics
```

## Description

Lấy dữ liệu tổng quan phục vụ dashboard AI.

## Authentication

Yêu cầu JWT và role ADMIN.

## Success Response

```json
{
  "success": true,
  "message": "AI analytics retrieved successfully",
  "data": {
    "totalAnalyzedComments": 1200,
    "positiveCount": 700,
    "neutralCount": 300,
    "negativeCount": 200
  }
}
```

---

# 10. AI API Summary

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| /api/v1/ai/analyze-comment/{commentId} | POST | Phân tích sentiment cho comment | Admin |
| /api/v1/ai/comments/{commentId}/sentiment | GET | Lấy sentiment theo comment | Admin |
| /api/v1/ai/products/{productId}/summary | GET | Lấy sentiment summary theo product | No / Yes |
| /api/v1/ai/products/{productId}/recalculate | POST | Tính lại sentiment summary | Admin |
| /api/v1/ai/comments/{commentId}/label | PATCH | Gán nhãn sentiment thủ công | Admin |
| /api/v1/ai/settings | GET | Lấy AI settings | Admin |
| /api/v1/ai/settings | PUT | Cập nhật AI settings | Admin |
| /api/v1/ai/analytics | GET | Lấy dữ liệu AI dashboard | Admin |