package com.techblog.domain.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSpecRequest {

    @NotBlank(message = "Spec key is required")
    private String specKey;

    @NotBlank(message = "Spec value is required")
    private String specValue;

    private String unit;

    @PositiveOrZero(message = "Display order must be greater than or equal to 0")
    private int displayOrder;
}