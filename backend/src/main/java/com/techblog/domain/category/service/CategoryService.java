package com.techblog.domain.category.service;

import com.techblog.domain.category.dto.CategoryResponse;
import com.techblog.domain.category.dto.CreateCategoryRequest;
import com.techblog.domain.category.model.Category;
import com.techblog.domain.category.repository.CategoryRepository;
// DÒNG IMPORT QUAN TRỌNG VỪA ĐƯỢC THÊM VÀO ĐÂY 👇
import com.techblog.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug đã tồn tại!");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setType(request.getType());

        category.setActive(true);

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục cha"));
            category.setParent(parent);
        }

        return mapToResponse(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void disableCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        category.setActive(false); // Tắt hiển thị
        categoryRepository.save(category);
    }

    @Transactional
    public void enableCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        category.setActive(true); // Đổi lại thành true để bật
        categoryRepository.save(category);
    }

    private CategoryResponse mapToResponse(Category category) {
        CategoryResponse res = new CategoryResponse();
        res.setId(category.getId());
        res.setName(category.getName());
        res.setSlug(category.getSlug());
        res.setType(category.getType());

        // Dùng isActive() của Entity và setIsActive() của DTO
        res.setIsActive(category.isActive());

        if (category.getParent() != null) {
            res.setParentId(category.getParent().getId());
            res.setParentName(category.getParent().getName());
        }
        return res;
    }

    // 1. Lấy chi tiết 1 danh mục theo ID
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        return mapToResponse(category);
    }

    // 2. Cập nhật danh mục
    @Transactional
    public CategoryResponse updateCategory(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        // Kiểm tra xem slug mới có bị trùng với danh mục khác không
        if (!category.getSlug().equals(request.getSlug()) && categoryRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug đã tồn tại!");
        }

        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setType(request.getType());

        // Cập nhật danh mục cha nếu có
        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục cha"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        return mapToResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        // 1. Tìm xem danh mục có tồn tại không
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        // 2. Kiểm tra an toàn: Nếu danh mục đang có bài viết thì KHÔNG CHO XÓA
        if (postRepository.existsByCategoryId(categoryId)) {
            throw new RuntimeException("Không thể xóa! Đang có bài viết thuộc danh mục này.");
        }

        // 3. Nếu không vướng bài viết nào, tiến hành xóa
        categoryRepository.delete(category);
    }
}