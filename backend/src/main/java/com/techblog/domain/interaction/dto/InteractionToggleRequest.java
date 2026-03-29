package com.techblog.domain.interaction.dto;

import com.techblog.common.enums.TargetType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InteractionToggleRequest {
    @NotNull(message = "Target type is required")
    private TargetType targetType;

    @NotNull(message = "Target ID is required")
    private Long targetId;
}
