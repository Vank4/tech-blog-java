package com.techblog.domain.ai.model;

import com.techblog.common.audit.Auditable;
import com.techblog.common.enums.SentimentConclusion;
import com.techblog.domain.product.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_sentiment_summary", uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_sentiment_summary_product", columnNames = "product_id")
})
public class ProductSentimentSummary extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "total_comments", nullable = false)
    private int totalComments;

    @Column(name = "positive_count", nullable = false)
    private int positiveCount;

    @Column(name = "negative_count", nullable = false)
    private int negativeCount;

    @Column(name = "neutral_count", nullable = false)
    private int neutralCount;

    @Column(name = "positive_ratio", nullable = false, precision = 6, scale = 2)
    private BigDecimal positiveRatio;

    @Column(name = "negative_ratio", nullable = false, precision = 6, scale = 2)
    private BigDecimal negativeRatio;

    @Column(name = "neutral_ratio", nullable = false, precision = 6, scale = 2)
    private BigDecimal neutralRatio;

    @Enumerated(EnumType.STRING)
    @Column(name = "conclusion", nullable = false, length = 30)
    private SentimentConclusion conclusion = SentimentConclusion.NOT_ENOUGH_DATA;

    @Column(name = "last_calculated_at")
    private LocalDateTime lastCalculatedAt;
}
