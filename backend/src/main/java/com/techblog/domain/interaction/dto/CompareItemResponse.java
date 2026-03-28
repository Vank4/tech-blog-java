package com.techblog.domain.interaction.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompareItemResponse {

    private Long itemId;
    private int position;
    private Long productId;
    private String name;
    private String slug;
    private String brand;
    private String model;
    private BigDecimal price;
    private String currency;
    private String thumbnailUrl;
    private BigDecimal ratingAverage;
    private int ratingCount;
    private BigDecimal reviewScore;
    private ProductSentimentResponse sentiment;
    private List<CompareProductSpecResponse> specs;
}