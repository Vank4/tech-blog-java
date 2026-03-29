package com.techblog.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDiscussionResponse {

    private Long id;
    private String headline;
    private String authorName;
    private String authorMeta;
    private String authorAvatarUrl;
    private String authorInitials;
    private String content;
    private LocalDateTime createdAt;
    private int helpfulCount;
    private int replyCount;
    private Integer starCount;
    private String badgeLabel;
    private boolean verified;
}
