package com.techblog.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageResponse {

    private Long id;
    private String imageUrl;
    private String altText;
    private Boolean primary;
    private Integer displayOrder;
}