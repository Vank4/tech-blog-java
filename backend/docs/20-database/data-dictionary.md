# Data Dictionary (Từ điển Dữ liệu MongoDB)

Dự án sử dụng Spring Data MongoDB. Dưới đây là sơ đồ các collections:

- **`users`**: Thông tin người dùng (Email, Password hash, First name, Last name, Role...).
- **`categories`**: Danh mục của blog và sản phẩm với field thiết kế đa bậc `parentId`.
- **`products`**: Thông tin chính sản phẩm (Name, Brand, URL Slug, Base Price). Nhúng (Embed) thêm data `specs` (list các thuộc tính máy) và `images`.
- **`posts`**: Nội dung bài viết (Content markup, Status, Tags, AuthorId).
- **`reviews`**: Lời bình luận chuyên sâu đánh giá một Target Product cụ thể. Kèm fields pros/cons.
- **`comments`**: Bình luận phía bên dưới bài viết/sản phẩm. Field `targetType` (POST/PRODUCT), `targetId`, `replyToId` (nếu là comment con), và data nhúng kết quả AI.
- **`ratings`**: Data user thả rating sao về một `productId`.
