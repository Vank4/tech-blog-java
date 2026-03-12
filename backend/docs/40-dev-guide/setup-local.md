# Hướng Dẫn Kéo & Chạy Dev Local (Local Environment)

Chào mừng đã vào dự án, để IDE setup ổn thỏa mà không lỗi build, hãy hoàn thiện đủ các bước máy tính bạn.

## 1. Yêu cầu Cài Hệ Sinh Thái Runtime Môi Trường Server
- **Ngôn ngữ Core**: Java Development Kit - JDK 17 (Cần set PATH ENV chuẩn xác).
- **Bộ máy biên dịch**: Maven 3.8+ (Gói Wrapper Mvnw đã nhúng).
- **Database Engine**: Cài Local MongoDB Community Server v6.0+ HOẶC bạn có thể xin Account MongoDB Cloud Atlas trỏ link remote.
- **Trình editor phát triển**: IntelliJ IDEA (Ultimate / Communtiy) vì khả năng support Lombok và Spring tốt nhất / Hoặc dùng VS Code (đã cài Extension Pack For Java).

## 2. Environment Variables & Kết Nối Backend
Kiểm tra biến cấu hình tại `backend/src/main/resources/application.yml` (nếu có file `application-dev.yml` vui lòng tự check ghi đè thông số Override lại của `application.yml`). Tham số tham khảo:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://127.0.0.1:27017/tech_review_db   # Đường link Data Cluster DB
      auto-index-creation: true

jwt:
  secret: DEFAULT_KEY_SECURITY_SECRET_DAI_IT_NHAT_CHIEU_DAI_256_BITS  # Khóa bảo mật ký token
  expiration: 86400000 # Config số msec thời hạn rụng Refresh. (1 ngày)
```

## 3. Khởi Xướng Run Server Bắt Đầu
- Mở **Command Line (Terminal)** điều hướng vào `d:\tech-blog-java\backend`.
- Ra lệnh dọn rác, pull JAR Maven từ repo public và Install dependencies: `mvn clean install -DskipTests` (hoặc nhấn chuột UI trong IDE).
- Nếu lệnh chạy thành công báo chữ **BUILD SUCCESS** màu xanh, hãy start Backend Web API: `mvn spring-boot:run`
- Server mặc định lắng nghe ở Root URL Port: `http://localhost:8080/`. Open page Swagger `http://localhost:8080/swagger-ui.html` để tham khảo chi tiết Request Body các API có hiệu lực trong project.
