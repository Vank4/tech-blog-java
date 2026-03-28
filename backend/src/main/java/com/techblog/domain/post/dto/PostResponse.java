package com.techblog.domain.post.dto;

import com.techblog.common.enums.ContentStatus;
import com.techblog.domain.tag.model.Tag;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String thumbnailUrl;
    private ContentStatus status;
    private long viewCount;
    private boolean allowComments;

    // Chỉ trả về ID và Tên của Danh mục & Tác giả cho gọn
    private Long categoryId;
    private String categoryName;
    private Long authorId;
    private String authorName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Set<Tag> tags;
    private boolean isFeatured;
    private int priority;
}