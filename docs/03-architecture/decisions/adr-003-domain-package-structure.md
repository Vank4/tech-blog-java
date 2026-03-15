# ADR-003: Use Domain-Based Package Structure

## Status

Accepted

---

## Context

Backend project cần một cách tổ chức code rõ ràng.

Hai cách tổ chức phổ biến:

1. Layer-based structure
2. Domain-based structure

Ví dụ Layer-based:

```
controller
service
repository
model
```

Ví dụ Domain-based:

```
product
user
post
comment
```

---

## Decision

Project quyết định sử dụng **Domain-based package structure**.

---

## Reasoning

Domain-based structure giúp:

1. Code được tổ chức theo business domain.
2. Mỗi module self-contained.
3. Dễ mở rộng hệ thống.
4. Dễ maintain code.
5. Giảm coupling giữa module.

Ví dụ:

```
domain/product
├── controller
├── service
├── repository
├── model
└── dto
```

Mỗi domain module có đầy đủ các layer của nó.

---

## Consequences

### Positive

- dễ phát triển theo module
- code rõ ràng
- dễ onboard developer mới

### Negative

- có thể trùng lặp code nếu không quản lý tốt
- cần convention rõ ràng

---

## Alternatives Considered

### Layer-based structure

Ví dụ:

```
controller
service
repository
entity
```

Ưu điểm:

- đơn giản
- dễ hiểu

Nhược điểm:

- code bị phân tán
- khó maintain khi project lớn

---

## Conclusion

Domain-based package structure phù hợp hơn cho backend project có nhiều module nghiệp vụ.