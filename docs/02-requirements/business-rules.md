# Business Rules

Các quy tắc nghiệp vụ của hệ thống.

---

# 1. User Rules

- Email của user phải duy nhất
- User bị ban không được login
- User phải verify email trước khi login

---

# 2. Rating Rules

- User chỉ được rating một sản phẩm một lần
- Rating nằm trong khoảng 1–5
- Rating có thể cập nhật

---

# 3. Post Rules

- Post phải có title
- Post phải thuộc category
- Post phải được admin duyệt trước khi publish

---

# 4. Review Rules

- Review phải gắn với product
- Review phải có overall score
- Review phải được admin duyệt

---

# 5. Comment Rules

- Comment phải thuộc một target (post hoặc product)
- Comment có thể reply
- Comment có thể bị admin hide

---

# 6. Report Rules

- User có thể report comment
- Admin phải xử lý report

---

# 7. AI Sentiment Rules

- Chỉ phân tích comment mới
- Sentiment được lưu vào database
- Admin có thể chỉnh sửa label nếu cần

---

# 8. Category Rules

- Category có thể có parent category
- Category bị disable sẽ không hiển thị

---

# 9. Product Rules

- Product phải có tên
- Product phải thuộc category
- Product có thể có nhiều ảnh