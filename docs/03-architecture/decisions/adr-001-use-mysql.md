# ADR-001: Use MySQL as Primary Database

## Status

Accepted

---

## Context

Hệ thống Tech Blog Java cần một database để lưu trữ dữ liệu cho:

- Users
- Products
- Posts
- Reviews
- Comments
- Ratings
- AI sentiment results

Database phải đáp ứng các yêu cầu:

- hỗ trợ relational data model
- hỗ trợ transaction
- ổn định và phổ biến
- dễ deploy
- dễ maintain
- tích hợp tốt với Spring Boot

Các lựa chọn database được xem xét:

- MySQL
- PostgreSQL
- MongoDB

---

## Decision

Hệ thống quyết định sử dụng **MySQL** làm database chính.

---

## Reasoning

MySQL được chọn vì các lý do sau:

1. Phù hợp với mô hình dữ liệu relational.
2. Tích hợp tốt với Spring Data JPA.
3. Phổ biến và có cộng đồng lớn.
4. Dễ triển khai trên nhiều môi trường.
5. Có hiệu năng tốt cho hệ thống CRUD.

Ngoài ra:

- Hibernate hỗ trợ MySQL rất tốt.
- MySQL dễ quản lý với các công cụ như HeidiSQL, DBeaver.

---

## Consequences

### Positive

- dễ phát triển
- dễ quản lý dữ liệu
- dễ backup
- dễ scale database

### Negative

- không phù hợp cho dữ liệu phi cấu trúc lớn
- cần tối ưu index khi dữ liệu lớn

---

## Alternatives Considered

### PostgreSQL

Ưu điểm:

- hỗ trợ nhiều tính năng advanced
- query mạnh hơn

Nhược điểm:

- phức tạp hơn MySQL
- team ít kinh nghiệm hơn

---

### MongoDB

Ưu điểm:

- linh hoạt với dữ liệu phi cấu trúc

Nhược điểm:

- không phù hợp với relational model
- khó enforce relationship

---

## Conclusion

MySQL là lựa chọn phù hợp nhất cho hệ thống Tech Blog Java backend.