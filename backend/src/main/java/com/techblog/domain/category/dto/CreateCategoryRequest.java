package com.techblog.domain.category.dto;

import com.techblog.common.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Slug không được để trống")
    private String slug;

    @NotNull(message = "Loại danh mục là bắt buộc")
    private CategoryType type;

    private Long parentId;
}