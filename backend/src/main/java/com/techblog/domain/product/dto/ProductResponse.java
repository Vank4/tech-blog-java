package com.techblog.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.techblog.common.enums.ProductStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    private Long id;
    private String name;
    private String slug;
    private String brand;
    private String model;
    private String shortDescription;
    private String description;
    private Long categoryId;
    private String categoryName;
    private String categorySlug;
    private BigDecimal price;
    private String currency;
    private ProductStatus status;
    private BigDecimal ratingAverage;
    private int ratingCount;
    private boolean allowComments;
    private LocalDateTime publishedAt;
    private String thumbnailUrl;
    private List<ProductImageResponse> images;
    private List<ProductSpecResponse> specs;
}