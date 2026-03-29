package com.techblog.domain.interaction.service;

import com.techblog.common.enums.TargetType;
import com.techblog.domain.interaction.dto.InteractionStatusResponse;

public interface InteractionService {
    InteractionStatusResponse toggleLike(Long userId, TargetType targetType, Long targetId);
    
    InteractionStatusResponse toggleFavorite(Long userId, TargetType targetType, Long targetId);
    
    InteractionStatusResponse getInteractionStatus(Long userId, TargetType targetType, Long targetId);
}
