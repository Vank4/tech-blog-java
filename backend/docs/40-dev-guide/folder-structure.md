# Cấu Trúc Architect Thư Mục (Folder Structure)

Dự án Java Spring Boot đang hiện hữu trong hệ thống Backend (`d:\tech-blog-java\backend\src\main\java\com\techblog\`) có cấu trúc thiết kế chia Module Layer (Giao thoa giữa Package By Layer và Package By Feature).

```text
src/main/java/com/techblog/
├── common/        # Chứa Abstract, Constant String Config, base API Request pagination, Audit Interfaces
├── config/        # Khu vực config Framework & Plugins (VD: MongoConfig, SwaggerConfig, CorsConfig)
├── security/      # Lớp lá chắn Authentication (Bộ lọc JWT Token Provider, JwtAuthenticationFilter)
├── modules/       # Tập đoàn logic Domain Service và Use-cases cốt lõi (sẽ tạo các package này)
│   ├── auth/      
│   ├── users/     
│   ├── products/  
│   ├── posts/     
│   └── comments/  
└── TechBlogJavaApplication.java # File runner khởi động nền tảng
```
*Luôn bám sát cấu trúc Domain Architecture này khi import thư viện tự tạo, không ném class vào chung bừa bãi.*
