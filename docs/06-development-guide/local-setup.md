# Local Setup

## 1. Purpose

Tài liệu này hướng dẫn cách setup môi trường local để chạy backend **Tech Blog Java**.

Mục tiêu:

- giúp thành viên trong team clone project và chạy được ngay
- chuẩn hóa môi trường phát triển
- giảm lỗi do khác biệt cấu hình giữa các máy

---

# 2. System Requirements

Trước khi chạy project, cần cài đặt các thành phần sau:

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 17+ | Runtime cho Spring Boot |
| Maven | 3.8+ | Build project |
| MySQL | 8+ | Database |
| Git | Latest | Clone source code |
| IntelliJ IDEA | Recommended | IDE phát triển |

Khuyến nghị:

- sử dụng IntelliJ IDEA
- sử dụng MySQL 8 trở lên
- sử dụng JDK 17

---

# 3. Clone Repository

Clone source code từ repository:

```bash
git clone <repository-url>
cd backend
```

---

# 4. Create Database

Tạo database trong MySQL:

```sql
CREATE DATABASE tech_blog_java CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Kiểm tra database đã tồn tại trước khi chạy application.

---

# 5. Configure Application

Cập nhật file:

```text
src/main/resources/application-dev.yml
```

Ví dụ cấu hình:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tech_blog_java?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh
    username: root
    password: your_password

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

Ngoài ra cần cấu hình:

- JWT secret
- mail settings nếu dùng
- port của server nếu cần đổi

---

# 6. Run Project

Có thể chạy project bằng Maven Wrapper:

```bash
./mvnw spring-boot:run
```

Hoặc trên Windows:

```bash
mvnw.cmd spring-boot:run
```

Nếu chạy bằng IDE:

- mở project bằng IntelliJ IDEA
- chạy class main:

```text
TechBlogJavaApplication
```

---

# 7. Verify Application Startup

Sau khi chạy thành công:

- application start không lỗi
- kết nối được database
- swagger mở được
- seed data chạy thành công nếu đã cấu hình

Mặc định app có thể chạy tại:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# 8. Verify Database

Sau khi app chạy, kiểm tra database đã có các bảng chính:

- users
- roles
- user_roles
- categories
- products
- posts
- reviews
- comments

Nếu dùng `ddl-auto=update`, Hibernate sẽ tự tạo bảng chưa tồn tại.

---

# 9. Seed Data Check

Kiểm tra seed data mặc định:

- roles: ADMIN, USER, AUTHOR
- tài khoản admin mặc định
- category mặc định

Nếu chưa có, kiểm tra class:

```text
DataSeeder
```

---

# 10. Common Startup Issues

## 10.1 Database Connection Failed

Nguyên nhân:

- MySQL chưa chạy
- sai username/password
- sai tên database

Cách xử lý:

- kiểm tra MySQL service
- kiểm tra cấu hình datasource
- kiểm tra database đã được tạo chưa

---

## 10.2 Port Already in Use

Nguyên nhân:

- cổng 8080 đang bị chiếm

Cách xử lý:

- đổi port trong `application-dev.yml`

Ví dụ:

```yaml
server:
  port: 8081
```

---

## 10.3 Java Version Mismatch

Nguyên nhân:

- dùng sai version Java

Cách xử lý:

- kiểm tra:

```bash
java -version
```

- đảm bảo đang dùng Java 17

---

## 10.4 Maven Build Failed

Nguyên nhân:

- thiếu dependency
- mạng lỗi khi tải package
- sai Java version

Cách xử lý:

```bash
./mvnw clean install
```

---

# 11. Recommended Local Workflow

Quy trình local được khuyến nghị:

1. Pull code mới nhất từ `develop`
2. Update database config local
3. Run project
4. Kiểm tra swagger
5. Tạo branch feature để code
6. Commit và push theo workflow của team

---

# 12. Local Setup Checklist

Checklist sau khi setup xong:

- [ ] Đã clone repository
- [ ] Đã tạo database
- [ ] Đã cấu hình `application-dev.yml`
- [ ] Đã chạy được project
- [ ] Đã truy cập được swagger
- [ ] Đã kiểm tra seed data
- [ ] Đã chạy được API test đầu tiên

---

# 13. Summary

Để chạy local backend cần:

- Java 17
- Maven
- MySQL
- cấu hình `application-dev.yml`
- chạy app bằng Maven hoặc IDE

Khi setup đúng, thành viên trong team phải có thể:

- chạy backend
- kết nối database
- test API qua Swagger
- bắt đầu code module được giao