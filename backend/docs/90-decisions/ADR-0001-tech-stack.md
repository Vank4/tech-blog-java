# ADR-0001: Lựa Chọn Cốt Lõi Về Tech Stack

## 1. Bối cảnh
Tech Review Platform gặp một thử thách bài toán về CSDL sản phẩm: Dữ liệu đồ điện tử có khối lượng thông số cấu hình phần cứng (Specifications - Specs) vô cùng biến động không nhất quán (Máy ảnh vs Laptop có specs khác rạch ròi 100%). Thêm vào đó, việc truy cập read/write bình luận Nested comment tree nhiều cấp phải diễn ra tức thời nhất định, tránh chênh độ trễ.

## 2. Quyết định của hệ thống
- Chọn **Java 17 kết hợp với Spring Boot 3.x Framework**.
  - **Why?**: Bộ đôi kinh điển cung cấp mức hiệu năng Enterprise, xử lý traffic đồng bộ, cung cấp Spring Security (thành phần bảo mật mạnh nhất để handle Authentication) và tính rõ ràng rành mạch trong RESTful API design.
- Thay vì SQL Database truyền thống (vd: MySQL, PostgrSQL) ta sử dụng **MongoDB (NoSQL)** Document Orientated Platform.
  - **Why?**: Mongo là cơ sở thiết kế JSON Schema-less, hỗ trợ embed hoàn toàn danh sách array linh hoạt cấu hình các thuộc tính key-value Specs thẳng cho `products`. Việc này gạt bỏ đi gánh nặng của thiết kế mô hình EAV (Entity Attribute Value) nặng nề chậm chạp trên SQL.
  
## 3. Rủi ro - Hệ quả đánh đổi
- Toàn bộ backend Dev trong team phải setup lại tư duy Modelling Entity, áp dụng việc gộp Embed đối tượng cha con với nhau thay vì tham chiếu qua lại Join bảng lỏng lẻo.
- Xử lý giao dịch nhiều thay đổi ở Spring Data cần được bọc lớp Annotation `@Transactional` (Yêu cầu Replica Set mới pass được) thay vì RDBMS hỗ trợ default native.
