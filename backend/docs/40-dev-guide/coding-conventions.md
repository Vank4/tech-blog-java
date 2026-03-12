# Convention Viết Code Dự Án (Code & Git)

## 1. Naming & Syntax Standards
- **Classes, Entities & Interfaces**: `PascalCase` (Ví dụ: `ProductService`, `PageRequestDto`).
- **Methods, Fields & Variables**: `camelCase` (Ví dụ: `createdAt`, `getUserByEmail()`).
- **Static Final Constants**: `UPPER_SNAKE_CASE` (Ví dụ: `MAX_PAGE_SIZE`, `TOKEN_EXPIRATION_TIME`).
- **Controller API Path**: Design chuẩn `kebab-case` và sử dụng danh từ số nhiều đối với collections. (Ví dụ: `@RequestMapping("/api/v1/product-categories")`).

## 2. Java / Spring Boot Best Practices
- Ưu tiên sử dụng Annotation `@RequiredArgsConstructor` (từ thư viện Lombok) để **Constructor Injection** các service thay vì dùng rải rác `@Autowired` trên mọi trường biến.
- Cần validation đầu vào? Nhét logic Validate ngay trên **DTO Object** từ thư viện `jakarta.validation` (Ví dụ: dùng `@NotBlank` hay `@Min(0)`).
- Không được trả một cục Model Entity Document của MongoDB phản hồi cho Controller trực tiếp để bay ra client (Risk Leak cấu trúc DB). Phải custom tạo các class Object riêng (như `ProductResponseDTO`).
- Việc bắt lỗi (Exception) phải gom chung ném vào throw lỗi và bắt nó trong Global Exception Handler Class (Có kèm Annotation `@RestControllerAdvice`).

## 3. Quản trị Source Code Branching Workflow 
- Có 2 nhánh trung tâm duy trì chạy dài:
  - `main`: Chạy Production server
  - `develop`: Trạng thái code nội bộ (Môi trường Staging/Dev)
- Phải kéo nhánh Feature cho bản thân để dev: `feature/ten-module` (VD: `feature/ai-integration-service`)
- Nhánh Hotfix (nếu production sập lỗi): `bugfix/mo-ta-loi` (VD: `bugfix/cors-upload-issue`)
