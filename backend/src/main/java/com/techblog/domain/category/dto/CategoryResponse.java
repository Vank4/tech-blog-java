package com.techblog.domain.category.dto;

import com.techblog.common.enums.CategoryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private CategoryType type;

    // Dùng Boolean (chữ B hoa) để Lombok tạo ra setIsActive và getIsActive
    private Boolean isActive;

    private Long parentId;
}