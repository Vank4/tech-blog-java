# Huong dan test API Rating + Compare

Tai lieu nay huong dan test toan bo feat 3 theo chuan `/api/v1`, bao gom luong dang nhap user, cham sao san pham va quan ly compare list mac dinh.

## 1. Dieu kien chuan bi

- Base URL: `http://localhost:8081`
- Backend Spring Boot dang chay
- MySQL dang chay va da migrate schema
- Can co it nhat 1 san pham o trang thai `PUBLISHED`
- User thuong phai dang nhap de test rating va compare

## 2. Dang nhap lay user token

- Method: `POST`
- URL: `{{baseUrl}}/api/v1/auth/login`
- Headers:
  - `Content-Type: application/json`
- Body:

```json
{
  "email": "user1@gmail.com",
  "password": "123456"
}
```

- Curl:

```bash
curl -X POST http://localhost:8081/api/v1/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"user1@gmail.com\",\"password\":\"123456\"}"
```

- Lay `data.accessToken` va gan vao header:

```text
Authorization: Bearer <userToken>
```

## 3. Rating API

### A. Cham sao lan dau

- Method: `POST`
- URL: `{{baseUrl}}/api/v1/products/{{productId}}/rating`
- Auth: `Bearer <userToken>`
- Body:

```json
{
  "rating": 5
}
```

- Response mong doi:

```json
{
  "success": true,
  "message": "Product rating saved successfully",
  "data": {
    "productId": 10,
    "ratingAverage": 5.00,
    "ratingCount": 1,
    "currentUserRating": 5
  }
}
```

### B. Cham sao lai cung product

- Method: `POST`
- URL: `{{baseUrl}}/api/v1/products/{{productId}}/rating`
- Auth: `Bearer <userToken>`
- Body:

```json
{
  "rating": 3
}
```

- Ky vong:
  - khong tao duplicate
  - `ratingCount` van giu nguyen
  - `currentUserRating` doi thanh `3`

### C. Xem rating summary public

- Method: `GET`
- URL: `{{baseUrl}}/api/v1/products/{{productId}}/rating-summary`
- Auth: khong bat buoc

- Neu gui token hop le, response co them `currentUserRating`.
- Neu khong gui token, `currentUserRating` se la `null`.

### D. Case loi can test

- `rating = 0` hoac `rating = 6` => `400 Validation failed`
- product khong ton tai hoac khong o trang thai `PUBLISHED` => `404 Product not found`
- khong gui token khi `POST /rating` => `401 Unauthorized`

## 4. Compare API

### A. Them product vao compare

- Method: `POST`
- URL: `{{baseUrl}}/api/v1/compare/items`
- Auth: `Bearer <userToken>`
- Body:

```json
{
  "productId": 10
}
```

- Response tra ve toan bo compare list hien tai.

### B. Them trung product

- Goi lai cung request tren.
- Ky vong:
  - khong tao duplicate item
  - compare list van hop le

### C. Lay compare list

- Method: `GET`
- URL: `{{baseUrl}}/api/v1/compare`
- Auth: `Bearer <userToken>`

- Moi item se gom:
  - thong tin product co ban
  - `thumbnailUrl`
  - `specs`
  - `ratingAverage`, `ratingCount`
  - `reviewScore`
  - `sentiment`

### D. Xoa product khoi compare

- Method: `DELETE`
- URL: `{{baseUrl}}/api/v1/compare/items/{{productId}}`
- Auth: `Bearer <userToken>`

- Ky vong:
  - item bi xoa thanh cong
  - goi lai `GET /api/v1/compare` se khong con product do

### E. Case loi can test

- add product khong `PUBLISHED` => `404 Product not found`
- xoa product khong co trong compare => `404 Compare item not found`
- khong gui token cho compare API => `401 Unauthorized`

## 5. Thu tu test nhanh de smoke test

1. Login user lay `userToken`
2. Goi `POST /api/v1/products/{productId}/rating` voi `rating = 5`
3. Goi lai `POST /api/v1/products/{productId}/rating` voi `rating = 3`
4. Goi `GET /api/v1/products/{productId}/rating-summary`
5. Goi `POST /api/v1/compare/items`
6. Goi lai `POST /api/v1/compare/items` de test add trung
7. Goi `GET /api/v1/compare`
8. Goi `DELETE /api/v1/compare/items/{productId}`
9. Goi lai `GET /api/v1/compare` de doi chieu

## 6. Ghi chu

- Feat 3 chi lam backend API, khong bao gom UI.
- Compare list dung 1 list mac dinh cho moi user.
- `reviewScore` duoc tinh theo trung binh `overallScore` cua cac review `PUBLISHED`.
- `sentiment` lay tu bang `product_sentiment_summary`, khong co du lieu thi tra ve `null`.