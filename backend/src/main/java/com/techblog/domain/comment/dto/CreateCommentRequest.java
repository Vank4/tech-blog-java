package com.techblog.domain.comment.dto;

import com.techblog.common.enums.TargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCommentRequest {
    @NotBlank(message = "Nội dung bình luận không được để trống")
    private String content;

    @NotNull(message = "Loại mục tiêu không được để trống")
    private TargetType targetType;

    @NotNull(message = "ID mục tiêu không được để trống")
    private Long targetId;

    private Long parentId;
}
