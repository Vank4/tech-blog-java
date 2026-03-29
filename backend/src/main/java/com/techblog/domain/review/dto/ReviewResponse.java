package com.techblog.domain.review.dto;

import com.techblog.common.enums.ContentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String productSlug;
    private String productImageUrl;
    private Long authorId;
    private String authorName;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String pros;
    private String cons;
    private BigDecimal overallScore;
    private ContentStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime publishedAt;
    private List<ReviewScoreDTO> scores;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean allowComments;

    @Getter
    @Setter
    @Builder
    public static class ReviewScoreDTO {
        private String criterion;
        private BigDecimal score;
        private BigDecimal maxScore;
        private String note;
        private int displayOrder;
    }
}
