# Environment Config

## 1. Purpose

Tài liệu này mô tả cấu hình môi trường của backend **Tech Blog Java**.

Mục tiêu:

- giải thích ý nghĩa các biến cấu hình
- chuẩn hóa cách sử dụng config giữa các môi trường
- giảm lỗi khi deploy hoặc chạy local

Project sử dụng cấu hình theo **Spring Profiles**.

---

# 2. Configuration Files

Các file cấu hình chính:

```text
application.yml
application-dev.yml
application-prod.yml
```

Ý nghĩa:

- `application.yml`: cấu hình chung
- `application-dev.yml`: cấu hình cho môi trường development
- `application-prod.yml`: cấu hình cho production

---

# 3. Active Profile

Profile hoạt động được xác định bởi:

```yaml
spring:
  profiles:
    active: dev
```

Hoặc truyền qua command line:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

---

# 4. Main Configuration Groups

Các nhóm cấu hình chính:

- server
- datasource
- jpa
- security
- logging
- mail
- swagger
- custom app settings

---

# 5. Server Configuration

Ví dụ:

```yaml
server:
  port: 8080
```

Ý nghĩa:

- cổng chạy backend application

Khuyến nghị:

- dev: `8080`
- prod: tùy môi trường deploy

---

# 6. Datasource Configuration

Ví dụ:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tech_blog_java?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

Ý nghĩa:

- `url`: chuỗi kết nối DB
- `username`: tài khoản DB
- `password`: mật khẩu DB
- `driver-class-name`: JDBC driver

---

# 7. JPA / Hibernate Configuration

Ví dụ:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

Ý nghĩa:

- `ddl-auto=update`: tự update schema trong dev
- `show-sql=true`: in SQL ra console
- `format_sql=true`: format SQL dễ đọc

Khuyến nghị:

- dev: `update`
- prod: `validate` hoặc dùng migration tool

---

# 8. JWT Configuration

Ví dụ:

```yaml
app:
  jwt:
    secret: your-secret-key
    expiration: 86400000
```

Ý nghĩa:

- `secret`: key ký JWT
- `expiration`: thời gian sống của token (milliseconds)

Lưu ý:

- production phải dùng secret mạnh
- không commit secret thật lên repository public

---

# 9. Mail Configuration

Nếu hệ thống có verify email / reset password:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your_email
    password: your_app_password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

Dùng cho:

- verify email
- reset password
- notification email

---

# 10. Logging Configuration

Ví dụ:

```yaml
logging:
  level:
    root: INFO
    com.techblog: DEBUG
```

Ý nghĩa:

- `root`: mức log mặc định
- `com.techblog`: mức log cho application package

Ngoài ra dùng file:

```text
logback-spring.xml
```

---

# 11. Swagger / OpenAPI Configuration

Có thể cấu hình:

- enable/disable swagger
- title
- version
- security scheme

Ví dụ custom config nằm trong:

```text
SwaggerConfig
```

---

# 12. App Custom Properties

Project có thể dùng custom properties:

```yaml
app:
  frontend-url: http://localhost:3000
  default-page-size: 10
  upload-dir: uploads
```

Dùng cho:

- CORS
- pagination
- file storage
- callback URLs

---

# 13. Environment Differences

## Development

Đặc điểm:

- bật `show-sql`
- dùng DB local
- bật swagger
- log chi tiết hơn

## Production

Đặc điểm:

- tắt `show-sql`
- dùng DB production
- tối ưu logging
- secret lấy từ environment variables
- cấu hình bảo mật chặt hơn

---

# 14. Recommended Sensitive Values Handling

Các giá trị nhạy cảm không nên hardcode trực tiếp.

Khuyến nghị dùng:

- environment variables
- secret manager
- CI/CD injected values

Ví dụ:

- database password
- JWT secret
- mail password

---

# 15. Config Checklist

Checklist cấu hình local:

- [ ] datasource đúng
- [ ] database tồn tại
- [ ] JWT secret đã cấu hình
- [ ] mail config đã cấu hình nếu dùng
- [ ] server port không bị conflict
- [ ] active profile đúng

---

# 16. Summary

Project dùng cấu hình theo profile:

- `application.yml`
- `application-dev.yml`
- `application-prod.yml`

Các cấu hình quan trọng nhất gồm:

- datasource
- jpa
- jwt
- server
- logging
- mail

Việc cấu hình đúng giúp:

- app chạy ổn định
- dễ chuyển môi trường
- giảm lỗi khi development và deployment