# Entity Modeling (ERD / Document Design)

Dự án ứng dụng cấu trúc **Denormalization (Phi chuẩn hóa)** và **Embedding (Nhúng document)** đặc trưng của CSDL MongoDB thay cho việc lạm dụng Relational Join bảng.

## Các quyết định thiết kế Model:

### 1. Embed (Nhúng vào chung 1 query)
- Field `images` và `specs` được list trực tiếp thành array ở trong Document `products`. Nhờ đó truy vấn API Product Detail không cần thiết lập $lookup dư thừa, cho tốc độ O(1).
- `moderation_logs` gắn trực tiếp vào Document `posts`.
- `aiAnalysis` (kết quả điểm/cấp bậc cho một bình luận) nhúng trực tiếp vào Collection `comments`.

### 2. Reference (Tham chiếu)
- `comments` giữ tham chiếu ObjectId tới `userId` và `targetId` (Product/Post). Lý do là vì số lượng list Comment có thể tăng liên tục (Unbounded Growth), ta tuyệt đối không thể embed hàng ngàn comments vào một Document `products`.
- `reviews` refer đến `products`.

### 3. Pre-computed Values / Data Cache
- Các số liệu được query liên tục như `ratingSummary` (Tỉ lệ 5 sao tổng) và `sentimentSummary` (Phần trăm sắc thái tích cực) đều được lưu cache cứng tại fields nằm trong bảng `products`. 
- Sự đồng bộ diễn ra với Trigger/Event Listener: Mỗi khi comment/rating mới xuất hiện, worker tự động map lại chỉ số này cho `products`.
