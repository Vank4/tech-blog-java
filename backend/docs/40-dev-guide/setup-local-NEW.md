# Hướng Dẫn Kéo & Chạy Dev Local (Local Environment)

Chào mừng đã vào dự án! Để IDE setup ổn thỏa mà không lỗi build, hãy hoàn thiện đủ các bước trên máy tính của bạn.

## 1. Yêu Cầu Hệ Sinh Thái Runtime

### Bắt Buộc:
- **Java Development Kit (JDK) 21** - [Download từ Eclipse Adoptium](https://adoptium.net/)
  - Set `JAVA_HOME` environment variable
  - Verify: `java -version` & `javac -version`

- **Maven 3.8+** - [Download từ Apache Maven](https://maven.apache.org/download.cgi)
  - Set `MAVEN_HOME` environment variable
  - Verify: `mvn --version`

- **MySQL 8.0+** - Qua Laragon (Khuyến Nghị)
  - [Download Laragon](https://laragon.org/) - Cài Full (Apache + MySQL + PHP + Node.js)
  - Hoặc cài [MySQL Community Server](https://dev.mysql.com/downloads/mysql/) riêng lẻ

- **IDE**: IntelliJ IDEA hoặc VS Code + Extension Pack For Java

### Tùy Chọn:
- Git client (cho version control)
- Postman/Insomnia (API testing)

---

## 2. Cấu Hình Database & Environment Variables

### Option A: Sử Dụng Laragon (Khuyến Nghị cho Windows)

1. **Cài Laragon:**
   - Download & install từ https://laragon.org/
   - Chọn Full installation (gồm MySQL)

2. **Khởi động MySQL:**
   - Mở **Laragon Control Panel**
   - Click **Start** để bật tất cả services
   - Kiểm tra MySQL có checkmark ✓

3. **Tạo Database:**
   ```bash
   # Cách 1: MySQL CLI
   mysql -u root
   mysql> CREATE DATABASE tech_blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   mysql> EXIT;
   
   # Cách 2: phpMyAdmin (http://localhost/phpmyadmin)
   # Login: root / (trống password)
   # Tạo database "tech_blog"
   ```

4. **Config trong application.yml:**
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/tech_blog?useSSL=false&serverTimezone=UTC
       username: root
       password:  # Laragon mặc định: trống
       driver-class-name: com.mysql.cj.jdbc.Driver
   ```

### Option B: MySQL Standalone

1. **Cài MySQL:**
   - Cài Community Server từ https://dev.mysql.com/downloads/mysql/
   - Ghi nhớ port (mặc định 3306)

2. **Tạo Database:**
   ```bash
   mysql -u root -p
   Enter password: ****
   mysql> CREATE DATABASE tech_blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   mysql> EXIT;
   ```

3. **Config:**
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/tech_blog?useSSL=false&serverTimezone=UTC
       username: root
       password: your_password
   ```

### Option C: Docker MySQL (Advanced)

```bash
# Chạy MySQL trong Docker
docker run --name tech-blog-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=tech_blog \
  -p 3306:3306 \
  -d mysql:8.0

# Verify connection
mysql -h localhost -u root -p
Enter password: root
```

---

## 3. Environment Variables

Tạo file `.env` tại `backend/` folder (nếu muốn override):

```env
# Database
DB_URL=jdbc:mysql://localhost:3306/tech_blog
DB_USERNAME=root
DB_PASSWORD=

# JWT (generate a strong secret key)
JWT_SECRET=your-very-long-256-bit-secret-key-change-this-in-production

# Server
SERVER_PORT=8081

# Logging
LOG_LEVEL=DEBUG
```

---

## 4. Khởi Xướng & Chạy Server

### Bước 1: Clone & Navigate
```bash
git clone <repo-url>
cd d:\tech-blog-java\backend
```

### Bước 2: Build & Install Dependencies
```bash
# Clean build (xóa cache cũ)
mvn clean install -DskipTests
```

**Nếu thành công, bạn sẽ thấy:**
```
[INFO] --------
[INFO] BUILD SUCCESS
[INFO] --------
```

### Bước 3: Chạy Spring Boot Server

#### Option A: Maven
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

#### Option B: JAR
```bash
mvn clean package -DskipTests
java -jar target/tech-blog-java-0.0.1-SNAPSHOT.jar
```

### Bước 4: Verify Server Started

Nếu thành công, console sẽ hiện:
```
Started TechBlogJavaApplication in X.XXX seconds
Tech Blog Java Application Started
```

---

## 5. Truy Cập & Test API

| Tài Nguyên | URL |
|-----------|-----|
| **Swagger UI** | http://localhost:8081/swagger-ui.html |
| **API Docs** | http://localhost:8081/v3/api-docs |
| **Health Check** | http://localhost:8081/actuator/health |

---

## 🛠️ Troubleshooting

### ❌ Error: "Cannot connect to database"
```
Giải pháp:
1. Check MySQL service running: Laragon Control Panel → Start
2. Check database exists: mysql -u root → SHOW DATABASES;
3. Check application.yml: datasource.url = jdbc:mysql://localhost:3306/tech_blog
```

### ❌ Error: "Port 8081 already in use"
```
Giải pháp:
1. Kill process: netstat -ano | findstr :8081 → taskkill /PID <PID> /F
2. Hoặc change port: application.yml → server.port: 8082
```

---

## 📚 File Tài Liệu Quan Trọng

- [MIGRATION_MONGODB_TO_MYSQL.md](MIGRATION_MONGODB_TO_MYSQL.md) - Chi tiết migration
- [SETUP_LARAGON_MYSQL.md](../SETUP_LARAGON_MYSQL.md) - Setup Laragon
- [API Documentation](../30-api/README.md) - API specs

---

## ✅ Checklist Setup Hoàn Tất

- [ ] JDK 21 cài & PATH setup
- [ ] Maven cài & PATH setup  
- [ ] Laragon started với MySQL running
- [ ] Database `tech_blog` created
- [ ] `mvn clean install -DskipTests` ✓
- [ ] `mvn spring-boot:run` ✓
- [ ] Server running: http://localhost:8081/swagger-ui.html ✓
