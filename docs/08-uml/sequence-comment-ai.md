# Sequence Diagram - Comment and AI Sentiment

## 1. Purpose

Tài liệu này mô tả **sequence flow giữa Comment Module và AI Module** trong hệ thống Tech Blog Java.

Mục tiêu:

- mô tả luồng tạo comment
- mô tả luồng gọi AI phân tích sentiment
- mô tả luồng lưu kết quả AI
- mô tả luồng tổng hợp sentiment theo product

---

# 2. Main Scenario: Create Comment

Actor tham gia:

- User
- CommentController
- CommentService
- CommentRepository
- Target Repository (Post/Review/Product)

---

# 3. Create Comment Sequence

```text
User
 → CommentController: POST /api/v1/comments
CommentController
 → CommentService: createComment(request)
CommentService
 → Target Repository: validate target exists
Target Repository
 → CommentService: target found
CommentService
 → CommentRepository: save(Comment with status = PENDING or APPROVED)
CommentRepository
 → CommentService: Comment
CommentService
 → CommentController: CommentResponse
CommentController
 → User: ApiResponse<CommentResponse>
```

---

# 4. Main Scenario: Auto AI Analysis After Comment Creation

Actor tham gia:

- CommentService
- AiService
- AiClient
- AiAnalysisResultRepository

---

# 5. AI Analyze Sequence

```text
CommentService
 → AiService: analyzeComment(commentId)
AiService
 → CommentRepository: findById(commentId)
CommentRepository
 → AiService: Comment
AiService
 → AiClient: analyze(comment.content)
AiClient
 → AiService: sentiment result
AiService
 → AiAnalysisResultRepository: save(result)
AiAnalysisResultRepository
 → AiService: saved
AiService
 → return success
```

---

# 6. Alternative Scenario: Manual AI Trigger by Admin

```text
Admin
 → AiController: POST /api/v1/ai/analyze-comment/{commentId}
AiController
 → AiService: analyzeComment(commentId)
AiService
 → CommentRepository: findById(commentId)
CommentRepository
 → AiService: Comment
AiService
 → AiClient: analyze(comment.content)
AiClient
 → AiService: sentiment result
AiService
 → AiAnalysisResultRepository: save(result)
AiService
 → AiController: AiResponse
AiController
 → Admin: ApiResponse
```

---

# 7. Main Scenario: Recalculate Product Sentiment Summary

Actor tham gia:

- Admin
- AiController
- AiService
- CommentRepository
- AiAnalysisResultRepository
- ProductRepository

---

# 8. Recalculate Sequence

```text
Admin
 → AiController: POST /api/v1/ai/products/{productId}/recalculate
AiController
 → AiService: recalculateProductSummary(productId)
AiService
 → ProductRepository: findById(productId)
ProductRepository
 → AiService: Product
AiService
 → CommentRepository: findCommentsByProductId(productId)
CommentRepository
 → AiService: comments
AiService
 → AiAnalysisResultRepository: findResultsByCommentIds(commentIds)
AiAnalysisResultRepository
 → AiService: sentiment results
AiService
 → AiService: aggregate counts (positive, neutral, negative)
AiService
 → AiService: compute conclusion
AiService
 → AiController: ProductSentimentSummaryResponse
AiController
 → Admin: ApiResponse
```

---

# 9. Main Scenario: Manual Labeling

```text
Admin
 → AiController: PATCH /api/v1/ai/comments/{commentId}/label
AiController
 → AiService: labelCommentSentiment(commentId, request)
AiService
 → AiAnalysisResultRepository: findByCommentId(commentId)
AiAnalysisResultRepository
 → AiService: existing result
AiService
 → AiAnalysisResultRepository: update label
AiService
 → AiController: success
AiController
 → Admin: ApiResponse
```

---

# 10. Detailed Sentiment Flow Notes

Kết quả AI thường gồm:

- sentimentLabel
- confidenceScore
- rawResult

Ví dụ:

```text
POSITIVE
confidence = 0.94
```

Dữ liệu này được lưu vào `ai_analysis_results`.

---

# 11. Product Summary Logic

Sau khi lấy danh sách sentiment results của product, hệ thống tính:

- positiveCount
- neutralCount
- negativeCount
- totalAnalyzedComments

Sau đó xác định `conclusion`:

- GOOD
- NEUTRAL
- BAD
- NOT_ENOUGH_DATA

---

# 12. Validation Points

Trong flow này cần validate:

- comment có tồn tại không
- product có tồn tại không
- AI module có đang bật không
- comment có đủ điều kiện để analyze không
- manual label có hợp lệ không

---

# 13. Error Scenarios

## Comment Not Found

```text
AiService
 → CommentRepository: findById(commentId)
CommentRepository
 → AiService: null
AiService
 → GlobalExceptionHandler: Comment not found
```

## AI Service Unavailable

```text
AiService
 → AiClient: analyze(...)
AiClient
 → AiService: error / timeout
AiService
 → GlobalExceptionHandler: AI service unavailable
```

---

# 14. Related Components

Các component chính:

- `CommentController`
- `CommentService`
- `CommentRepository`
- `AiController`
- `AiService`
- `AiClient`
- `AiAnalysisResultRepository`

---

# 15. Summary

Comment-AI sequence của hệ thống gồm:

1. User tạo comment
2. Hệ thống hoặc admin kích hoạt AI analysis
3. AI result được lưu
4. Product sentiment summary có thể được tính lại

Flow này giúp:

- gắn AI trực tiếp vào dữ liệu comment
- tạo insight cộng đồng theo sản phẩm
- hỗ trợ admin kiểm soát và chỉnh sửa nhãn khi cần