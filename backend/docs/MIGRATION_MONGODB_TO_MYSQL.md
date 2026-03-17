# Migration Guide: MongoDB → MySQL

## 📋 Tóm tắt Thay Đổi

Dự án **Tech Blog Java** đã chuyển đổi từ **MongoDB (NoSQL)** sang **MySQL (SQL Relational Database)** để tối ưu hóa với Laragon environment.

---

## 🔄 Thay Đổi Chính

### 1. **Dependencies (pom.xml)**

#### ❌ Loại bỏ:
```xml
<!-- MongoDB (cũ) -->
<spring-boot-starter-data-mongodb>
```

#### ✅ Thêm vào:
```xml
<!-- JPA/Hibernate -->
<spring-boot-starter-data-jpa>

<!-- MySQL Driver -->
<mysql-connector-j>

<!-- Validation -->
<spring-boot-starter-validation>

<!-- Lombok (để giảm boilerplate) -->
<lombok>

<!-- Swagger/OpenAPI -->
<springdoc-openapi-starter-webmvc-ui>

<!-- Spring Security & JWT -->
<spring-boot-starter-security>
<jjwt-api>, <jjwt-impl>, <jjwt-jackson>
```

**Status:** ✅ **HOÀN THÀNH**

---

### 2. **Configuration Files**

#### `application.yml` (Main Config)
- ❌ Loại bỏ: `spring.data.mongodb.*`
- ✅ Thêm: `spring.datasource.*` (MySQL connection)
- ✅ Thêm: `spring.jpa.hibernate.*` (Hibernate ORM config)

**Status:** ✅ **HOÀN THÀNH**

#### `application-dev.yml` (Development)
- ✅ Cấu hình MySQL kết nối **Laragon** (localhost:3306)
- ✅ `ddl-auto: create-drop` → Tự động tạo/xóa table
- ✅ `show-sql: true` → Log SQL queries
- ✅ `open-in-view: true` → Lazy loading support

**Status:** ✅ **HOÀN THÀNH**

#### `application-prod.yml` (Production)
- ✅ Cấu hình MySQL với Environment Variables
- ✅ `ddl-auto: validate` → Chỉ validate, không sửa đổi
- ✅ Connection pooling (HikariCP)
- ✅ Server compression & logging

**Status:** ✅ **HOÀN THÀNH**

---

### 3. **Domain Models - Cần Thay Đổi**

#### ❌ **MongoDB Document** (Cũ):
```java
@Document(collection = "users")
@Data
public class User {
    @Id
    private ObjectId id;
    private String email;
    private List<String> roles;
    // ...
}
```

#### ✅ **JPA Entity** (Mới - cần implement):
```java
@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;
    // ...
}
```

**Key Changes:**
- `@Document` → `@Entity`
- `@Id ObjectId` → `@Id @GeneratedValue Long`
- `List<>` → `Set<>` (many-to-many collection)
- `@Column` annotations cho constraints

**Status:** ⏳ **CẦN IMPLEMENT**

---

### 4. **Repository Pattern**

#### ❌ MongoDB Repository (Cũ):
```java
// Spring Data MongoDB
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByEmail(String email);
}
```

#### ✅ JPA Repository (Mới):
```java
// Spring Data JPA
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

**Status:** ⏳ **CẦN IMPLEMENT**

---

### 5. **Configuration Classes**

#### ✅ `MongoConfig.java` → Updated
- Đổi từ `@EnableMongoRepositories` → `@EnableJpaRepositories`
- Thêm `@EnableTransactionManagement`

**Status:** ✅ **HOÀN THÀNH**

#### ✅ `SecurityConfig.java`
- ✅ Enabled Spring Security
- ✅ JWT filter chain configured
- ✅ Authentication entry point set

**Status:** ✅ **HOÀN THÀNH**

#### ✅ `JwtTokenProvider.java`
- ✅ Token generation/validation implemented

**Status:** ✅ **HOÀN THÀNH**

#### ✅ `JwtAuthenticationFilter.java`
- ✅ Created in `security/filter/` folder
- ✅ Token extraction & validation

**Status:** ✅ **HOÀN THÀNH**

#### ✅ `AuthEntryPoint.java`
- ✅ Created in `security/handler/` folder
- ✅ 401 error handling

**Status:** ✅ **HOÀN THÀNH**

#### ✅ `CorsConfig.java`
- ✅ CORS mapping configured

**Status:** ✅ **HOÀN THÀNH**

#### ✅ `SwaggerConfig.java`
- ✅ OpenAPI/Swagger UI configured

**Status:** ✅ **HOÀN THÀNH**

#### ✅ `AppProperties.java`
- ✅ Configuration properties binding

**Status:** ✅ **HOÀN THÀNH**

---

### 6. **Constants**

#### `AppConstants.java`
- ✅ Đổi `COLLECTION_*` → `TABLE_*`
- ✅ Thêm status constants (POST_STATUS_*, SENTIMENT_*, etc.)

**Status:** ✅ **HOÀN THÀNH**

---

### 7. **Database Schema Design - Cần Tạo**

**MongoDB (Cũ) → MySQL (Mới) Mapping:**

| Aspect | MongoDB (Document) | MySQL (Relational) |
|--------|-------------------|-------------------|
| **Users** | Collection | `users` table |
| **Posts** | Collection + embed images/specs | `posts` table + `post_images` table |
| **Products** | Collection + nested specs | `products` table + `product_specs` table |
| **Comments** | Collection (nested) | `comments` table + `comment_replies` table |
| **Ratings** | Collection | `ratings` table |
| **Reviews** | Collection | `reviews` table |

**Status:** ⏳ **CẦN IMPLEMENT**

---

## 🔧 Quy Trình Chuyển Đổi Domain Entities

### Bước 1: Tạo Entity Classes
```
src/main/java/com/techblog/domain/{module}/model/
├── User.java
├── Post.java
├── Product.java
├── Comment.java
├── Rating.java
└── Review.java
```

### Bước 2: Tạo Repository Interfaces
```
src/main/java/com/techblog/domain/{module}/repository/
├── UserRepository.java
├── PostRepository.java
├── ProductRepository.java
└── ...
```

### Bước 3: Tạo Service Classes
```
src/main/java/com/techblog/domain/{module}/service/
├── UserService.java
├── PostService.java
└── ...
```

### Bước 4: Tạo Request/Response DTOs
```
src/main/java/com/techblog/domain/{module}/dto/
├── UserRequest.java
├── UserResponse.java
├── PostRequest.java
└── ...
```

### Bước 5: Tạo Controller Classes
```
src/main/java/com/techblog/domain/{module}/controller/
├── UserController.java
├── PostController.java
└── ...
```

---

## 📊 Database Setup (Laragon)

### 1. Tạo Database
```sql
CREATE DATABASE tech_blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Hibernate sẽ tự động tạo bảng
- Development: `ddl-auto: create-drop` → Tự động tạo/xóa
- Production: `ddl-auto: validate` → Chỉ validate

---

## ⚙️ Environment Variables (Production)

```env
# MySQL
DB_URL=jdbc:mysql://your-host:3306/tech_blog?useSSL=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your-password
DB_POOL_SIZE=20
DB_MIN_IDLE=5

# JWT
JWT_SECRET=your-256-bit-secret-key-change-me
JWT_EXPIRATION=86400000

# CORS
CORS_ALLOWED_ORIGINS=https://yourdomain.com

# Server
SERVER_PORT=8080
```

---

## 🚀 Chạy Application

### Development (Laragon)
```bash
cd backend
mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Production
```bash
java -jar target/tech-blog-java-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --server.port=8080 \
  --app.jwt.secret=your-secret-key
```

### Swagger UI
- Development: `http://localhost:8081/swagger-ui.html`
- Production: `http://your-domain/swagger-ui.html`

---

## 📝 Danh Sách Công Việc Còn Lại

- [ ] Implement User Entity + Repository + Service + Controller
- [ ] Implement Auth Module (Login/Register/Refresh Token)
- [ ] Implement Post Entity + full CRUD
- [ ] Implement Product Entity + full CRUD
- [ ] Implement Comment Entity + nested reply
- [ ] Implement Rating Entity
- [ ] Implement Review Entity
- [ ] Implement AI Sentiment Analysis Service (async)
- [ ] Implement File Upload Service
- [ ] Implement Email Service
- [ ] Add unit tests & integration tests
- [ ] Setup database migration (Flyway/Liquibase)
- [ ] Deploy to production

---

## 🛠️ Troubleshooting

### Error: "MySQL JDBC Connection Failed"
```
Solution: Chắc chắn MySQL service đang chạy (Laragon Control Panel)
         Check DB_URL, DB_USERNAME, DB_PASSWORD
```

### Error: "Cannot acquire a connection"
```
Solution: Database không tồn tại hoặc HikariCP pool exhausted
         CREATE DATABASE tech_blog (nếu chưa có)
         Tăng DB_POOL_SIZE trong environment
```

### Error: "JwtAuthenticationFilter not found"
```
Solution: Đảm bảo JwtAuthenticationFilter.java nằm trong security/filter/
         Rebuild Maven: mvn clean compile
```

---

## 📚 Tài Liệu Tham Khảo

- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Hibernate ORM Guide](https://hibernate.org/orm/documentation/)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT (JJWT) Examples](https://github.com/jwtk/jjwt)
- [MySQL 8.0 Reference Manual](https://dev.mysql.com/doc/)
