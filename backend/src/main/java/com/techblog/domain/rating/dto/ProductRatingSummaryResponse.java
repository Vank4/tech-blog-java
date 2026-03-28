package com.techblog.domain.rating.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRatingSummaryResponse {

    private Long productId;
    private BigDecimal ratingAverage;
    private int ratingCount;
    private Integer currentUserRating;
}