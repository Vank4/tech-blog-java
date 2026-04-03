package com.techblog.domain.category.controller;

import com.techblog.common.response.ApiResponse;
import com.techblog.domain.category.dto.CategoryResponse;
import com.techblog.domain.category.dto.CreateCategoryRequest;
import com.techblog.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách thành công", categoryService.getAllCategories()));
    }

    @PostMapping
    // TODO: Bỏ comment dòng dưới khi có Login
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse data = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Tạo danh mục thành công", data));
    }

    @PutMapping("/{id}")
    // TODO: Bỏ comment dòng dưới khi có Login
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse data = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật danh mục thành công", data));
    }

    @PatchMapping("/{id}/disable")
    // TODO: Bỏ comment dòng dưới khi có Login
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> disable(@PathVariable Long id) {
        categoryService.disableCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã tắt danh mục", null));
    }

    @PatchMapping("/{id}/enable")
    // TODO: Bỏ comment dòng dưới khi có Login
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> enable(@PathVariable Long id) {
        categoryService.enableCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã bật lại danh mục", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(
                true, "Lấy chi tiết danh mục thành công", categoryService.getCategoryById(id)));
    }

    // THÊM API XÓA DANH MỤC VÀO ĐÂY
    @DeleteMapping("/{id}")
    // TODO: Bỏ comment dòng dưới khi có Login
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        // Đảm bảo trong CategoryService của bạn đã có hàm deleteCategory()
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã xóa danh mục thành công", null));
    }
}