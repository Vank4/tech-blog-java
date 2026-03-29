package com.techblog.domain.comment.dto;

import com.techblog.common.enums.CommentStatus;
import com.techblog.common.enums.TargetType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CommentResponse {
    private Long id;
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private String content;
    private TargetType targetType;
    private Long targetId;
    private Long parentId;
    private CommentStatus status;
    private int likeCount;
    private LocalDateTime createdAt;
    private List<CommentResponse> replies = new ArrayList<>();
}
