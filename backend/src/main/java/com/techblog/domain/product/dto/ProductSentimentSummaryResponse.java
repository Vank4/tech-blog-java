package com.techblog.domain.product.dto;

import com.techblog.common.enums.SentimentConclusion;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSentimentSummaryResponse {

    private int totalComments;
    private int positiveCount;
    private int negativeCount;
    private int neutralCount;
    private BigDecimal positiveRatio;
    private BigDecimal negativeRatio;
    private BigDecimal neutralRatio;
    private SentimentConclusion conclusion;
    private LocalDateTime lastCalculatedAt;
}