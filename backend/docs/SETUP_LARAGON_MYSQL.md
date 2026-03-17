# Hướng Dẫn Setup MySQL với Laragon

## 📋 Yêu Cầu Hệ Thống

- **Laragon** (v4.0+) - Local development environment
- **MySQL** (v8.0+) - Included in Laragon
- **Java JDK 21**
- **Maven 3.8+**

---

## 🚀 Setup Laragon (Windows)

### 1. Cài Đặt Laragon

1. Download từ: https://laragon.org/
2. Cài đặt Laragon Full (includes Apache, MySQL, PHP, Node.js)
3. Chọn các components cần thiết (MySQL là bắt buộc)
4. Khởi chạy **Laragon Control Panel**

### 2. Khởi Chạy MySQL Service

1. Mở **Laragon Control Panel**
2. Click nút **Start** (hoặc tất cả services)
3. Đảm bảo **MySQL** có check mark ✓

```
Laragon Services:
✓ Apache
✓ MySQL
✓ Node.js
```

### 3. Tạo Database mới

#### Cách 1: Sử dụng MySQL CLI
```bash
# Mở Command Prompt/PowerShell
cd C:\laragon\bin\mysql\mysql-8.0-winx64\bin  # Path tuỳ version

# Hoặc dùng alias nếu Laragon đã add vào PATH
mysql -u root

# Tạo database
mysql> CREATE DATABASE tech_blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
mysql> USE tech_blog;
mysql> SHOW TABLES;  # Bảng sẽ được tạo tự động bởi Hibernate
mysql> EXIT;
```

#### Cách 2: Sử dụng Laragon GUI (phpMyAdmin)
1. Mở **Laragon Control Panel**
2. Click **Tools** → **PHPMyAdmin** (hoặc `http://localhost/phpmyadmin`)
3. Login: Username = `root`, Password = `` (trống)
4. Tạo database mới: **tech_blog**
5. Character Set: **utf8mb4_unicode_ci**

---

## 🔧 Cấu Hình Application.yml

### File: `backend/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tech_blog?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password:  # Laragon mặc định: trống (không có password)
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  jpa:
    hibernate:
      ddl-auto: update  # hoặc create-drop trong dev
```

### File: `backend/src/main/resources/application-dev.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tech_blog?useSSL=false&serverTimezone=UTC
    username: root
    password:  # Laragon: trống
    
  jpa:
    hibernate:
      ddl-auto: create-drop  # Tự động tạo table khi start
    show-sql: true  # Log SQL queries (dev only)
```

---

## ⚡ Chạy Application

### 1. Terminal: Build Project
```bash
cd d:\tech-blog-java\backend

# Clean build
mvn clean compile

# Install dependencies
mvn install -DskipTests
```

### 2. Terminal: Start Spring Boot

#### Option A: Sử dụng Maven
```bash
# Run with development profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Hoặc đơn giản (sẽ dùng application.yml mặc định)
mvn spring-boot:run
```

#### Option B: Build JAR & Run
```bash
# Build JAR
mvn clean package -DskipTests

# Run JAR
java -jar target/tech-blog-java-0.0.1-SNAPSHOT.jar
```

### 3. Kiểm Tra Kết Nối

Nếu thành công, bạn sẽ thấy:
```
Started TechBlogJavaApplication in X.XXX seconds (JVM running for X.XXX)
Tech Blog Java Application Started
```

---

## 🌐 Truy Cập Application

| Tài Nguyên | URL |
|-----------|-----|
| **API Health** | http://localhost:8081/actuator/health |
| **Swagger UI** | http://localhost:8081/swagger-ui.html |
| **API Docs** | http://localhost:8081/v3/api-docs |
| **MySQL (phpMyAdmin)** | http://localhost/phpmyadmin |

---

## 🔍 Kiểm Tra Database

### Cách 1: MySQL CLI
```bash
mysql -u root

mysql> USE tech_blog;

# Xem các bảng đã tạo
mysql> SHOW TABLES;

# Xem structure của table
mysql> DESCRIBE users;

# Xem dữ liệu
mysql> SELECT * FROM users;

mysql> EXIT;
```

### Cách 2: phpMyAdmin
1. Mở http://localhost/phpmyadmin
2. Login: `root` / `` (trống)
3. Chọn database `tech_blog`
4. Xem các bảng trong tab **Bảng**

---

## 🛠️ Troubleshooting

### ❌ Error: "Connection refused"
```
Problema: MySQL service không chạy
Giải pháp:
1. Mở Laragon Control Panel
2. Click Start (hoặc click checkbox MySQL)
3. Chờ dữ liệu khởi động
```

### ❌ Error: "Access denied for user 'root'"
```
Problema: Password không đúng
Giải pháp:
- Laragon mặc định: root / (trống)
- Check application.yml: password: 
- Nếu đặt password riêng, update application.yml
```

### ❌ Error: "Unknown database 'tech_blog'"
```
Problema: Database chưa tạo
Giải pháp:
mysql> CREATE DATABASE tech_blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### ❌ Error: "Port 8081 already in use"
```
Problema: Port 8081 đã được sử dụng
Giải pháp:
1. Option A: Kill process trên port 8081
   netstat -ano | findstr :8081
   taskkill /PID <PID> /F

2. Option B: Thay đổi port trong application.yml
   server:
     port: 8082
```

### ❌ Error: "No suitable driver found"
```
Problema: MySQL JDBC driver không được load
Giải pháp:
- Check pom.xml: <mysql-connector-j> dependency
- Run: mvn clean compile
- Rebuild IDE project
```

### ❌ Hibernate: "LazyInitializationException"
```
Problema: Lazy loading session đã đóng
Giải pháp: 
- application.yml: spring.jpa.open-in-view: true (dev only)
- Hoặc fetch EAGER thay vì LAZY
```

---

## 📋 Danh Sách PORTs Laragon Mặc Định

| Service | Port | URL |
|---------|------|-----|
| Apache | 80 | http://localhost |
| MySQL | 3306 | localhost:3306 |
| phpMyAdmin | 80 | http://localhost/phpmyadmin |
| Node.js (nếu cài) | 3000+ | http://localhost:3000+ |
| **Spring Boot** | **8081** | **http://localhost:8081** |

---

## 🔐 Tạo User MySQL mới (Optional)

Nếu muốn tạo user riêng cho app:

```bash
mysql -u root

mysql> CREATE USER 'techblog'@'localhost' IDENTIFIED BY 'secure_password';
mysql> GRANT ALL PRIVILEGES ON tech_blog.* TO 'techblog'@'localhost';
mysql> FLUSH PRIVILEGES;
mysql> EXIT;
```

Sau đó update `application.yml`:
```yaml
spring:
  datasource:
    username: techblog
    password: secure_password
```

---

## ✅ Checklist Setup

- [ ] Laragon cài đặt & MySQL service chạy
- [ ] Database `tech_blog` đã tạo
- [ ] JDK 21 & Maven cài đặt
- [ ] Clone project & cd vào `backend`
- [ ] `mvn clean compile` thành công
- [ ] `mvn spring-boot:run` database tables tự động tạo
- [ ] Truy cập http://localhost:8081/swagger-ui.html → OK
- [ ] phpMyAdmin check dữ liệu tables → OK

---

## 📞 Hỗ Trợ Thêm

Nếu gặp vấn đề:
1. Check logs trong console
2. Xem [MIGRATION_MONGODB_TO_MYSQL.md](MIGRATION_MONGODB_TO_MYSQL.md)
3. Xem [setup-local.md](40-dev-guide/setup-local.md)
