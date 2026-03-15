package com.techblog.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequest {
    private String name;
    private String slug;
    private String brand;
    private String model;
    private String shortDesc;
    private String description;
    private Long categoryId;
    private BigDecimal price;
    private String thumbnailUrl;
}