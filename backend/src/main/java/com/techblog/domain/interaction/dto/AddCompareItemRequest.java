package com.techblog.domain.interaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCompareItemRequest {

    @NotNull(message = "productId is required")
    private Long productId;
}