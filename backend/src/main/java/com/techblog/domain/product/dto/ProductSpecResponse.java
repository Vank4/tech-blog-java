package com.techblog.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSpecResponse {

    private Long id;
    private String specKey;
    private String specValue;
    private String unit;
    private int displayOrder;
}