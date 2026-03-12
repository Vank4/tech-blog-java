# Endpoints: Products (Quản lý thiết bị sản phẩm)

Trung tâm nghiệp vụ dữ liệu sản phẩm phần cứng dùng trong Product Core Module.

| HTTP | Endpoint | Giải thích / Mô tả | Quyền Auth |
|---|---|---|---|
| `GET`  | `/api/v1/products` | Danh mục tổng list Sản phẩm. Filter nhiều lớp: Price range, Brand... | `Public` |
| `GET`  | `/api/v1/products/{slug}` | Xem Profile máy. Chứa Specs Array, Rating summary average. | `Public` |
| `POST` | `/api/v1/admin/products` | Thao tác Insert Dữ liệu thiết bị mới (Admin-only). | `ADMIN` |
| `PUT`  | `/api/v1/admin/products/{id}` | Update (Thêm Specs, Sửa tên/Giá, đính kèm Object ảnh). | `ADMIN` |
| `PUT`  | `/api/v1/admin/products/{id}/status`| Quản lý trạng thái Visibility (Visible / Hidden). | `ADMIN` |
| `POST` | `/api/v1/products/{id}/rating` | Hành động thả Rating Star (1-5 Sao) sau khi trải nghiệm sp của User. | `USER/AUTHOR` |
