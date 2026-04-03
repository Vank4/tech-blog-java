package com.techblog.domain.product.dto;

import com.techblog.common.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private ProductStatus status;
}