package com.techblog.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePostRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    private String summary;
    private String thumbnailUrl;

    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;

    private boolean allowComments;
}