package com.techblog.domain.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageRequest {

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    private String altText;

    private boolean primary;

    @PositiveOrZero(message = "Display order must be greater than or equal to 0")
    private int displayOrder;
}