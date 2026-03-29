package com.techblog.domain.interaction.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InteractionStatusResponse {
    private boolean isLiked;
    private boolean isSaved;
    private long likeCount;
    private long saveCount;
}
