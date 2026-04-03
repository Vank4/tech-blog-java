package com.techblog.domain.review.model;

import com.techblog.common.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "review_scores")
public class ReviewScore extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "criterion", nullable = false, length = 120)
    private String criterion;

    @Column(name = "score", nullable = false, precision = 4, scale = 2)
    private BigDecimal score;

    @Column(name = "max_score", nullable = false, precision = 4, scale = 2)
    private BigDecimal maxScore;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;
}
