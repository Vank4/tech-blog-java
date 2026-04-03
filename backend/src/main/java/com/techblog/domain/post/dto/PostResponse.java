package com.techblog.domain.post.dto;

import com.techblog.common.enums.ContentStatus;
import com.techblog.domain.tag.model.Tag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor  // Thêm cái này để tránh lỗi khi một số thư viện cần khởi tạo object trống
@AllArgsConstructor // Cần thiết để @Builder hoạt động chính xác
public class PostResponse {
    private Long id;
    private String title;
    private String slug;
    private String summary;


    private String content;

    private String thumbnailUrl;
    private ContentStatus status;
    private long viewCount;
    private boolean allowComments;

    // Thông tin danh mục & tác giả
    private Long categoryId;
    private String categoryName;
    private Long authorId;
    private String authorName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Set<Tag> tags;
    private boolean isFeatured;
    private int priority;

    private String rejectionReason;
}