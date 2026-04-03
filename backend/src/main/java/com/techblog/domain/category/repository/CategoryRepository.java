package com.techblog.domain.category.repository;

import com.techblog.common.enums.CategoryType;
import com.techblog.domain.category.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Category> findByTypeAndActiveTrueOrderBySortOrderAsc(CategoryType type);
}
