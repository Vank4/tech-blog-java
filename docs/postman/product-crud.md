# Product CRUD Postman Guide

## Scope

- Feature: Member 2 - Product CRUD
- Branch: `feature/product-crud`
- Phase: feat 1 only

## Collection file

- `docs/postman/product-crud.postman_collection.json`

## Variables

- `baseUrl`: default `http://localhost:8081`
- `adminToken`: JWT token of an admin account
- `categoryId`: existing category id in database
- `productId`: product id returned after create
- `productSlug`: slug of the product for public detail

## Endpoints in this collection

1. `GET /api/v1/products`
2. `GET /api/v1/products/{slug}`
3. `POST /api/v1/admin/products`
4. `PUT /api/v1/admin/products/{id}`
5. `PATCH /api/v1/admin/products/{id}/status`
6. `DELETE /api/v1/admin/products/{id}`
7. `PATCH /api/v1/admin/products/{id}/restore`

## Suggested test order

1. Login as admin and copy token to `adminToken`
2. Create product
3. Publish product
4. Get products
5. Get product by slug
6. Hide product
7. Publish again if you want it public
8. Soft delete product
9. Restore product

## Example create body

```json
{
  "name": "MacBook Pro M4",
  "slug": "macbook-pro-m4",
  "brand": "Apple",
  "model": "M4-14-2026",
  "shortDescription": "Laptop cho developer va creator",
  "description": "MacBook Pro M4 14 inch, 16GB RAM, 512GB SSD.",
  "categoryId": 1,
  "price": 45990000,
  "currency": "VND",
  "allowComments": true
}
```

## Example success response

```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 1,
    "name": "MacBook Pro M4",
    "slug": "macbook-pro-m4",
    "brand": "Apple",
    "model": "M4-14-2026",
    "shortDescription": "Laptop cho developer va creator",
    "description": "MacBook Pro M4 14 inch, 16GB RAM, 512GB SSD.",
    "categoryId": 1,
    "categoryName": "Laptop",
    "categorySlug": "laptop",
    "price": 45990000,
    "currency": "VND",
    "status": "DRAFT",
    "ratingAverage": 0.00,
    "ratingCount": 0,
    "allowComments": true,
    "publishedAt": null
  }
}
```

## Example validation error

```json
{
  "success": false,
  "message": "Validation failed",
  "errors": {
    "name": "Name is required"
  }
}
```

## Behavior notes

- Soft delete sets product status to `DELETED`
- Deleted products are hidden from public list and public detail
- Restore sets product back to `DRAFT`
- Restored products must be published again to become public
- Status endpoint only accepts `PUBLISHED` or `HIDDEN`