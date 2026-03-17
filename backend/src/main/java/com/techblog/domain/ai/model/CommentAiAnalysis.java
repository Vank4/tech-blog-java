package com.techblog.domain.ai.model;

import com.techblog.common.audit.Auditable;
import com.techblog.common.enums.SentimentLabel;
import com.techblog.domain.comment.model.Comment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment_ai_analysis", uniqueConstraints = {
        @UniqueConstraint(name = "uk_comment_ai_analysis_comment_model", columnNames = { "comment_id", "model_id" })
})
public class CommentAiAnalysis extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private MlModel model;

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false, length = 20)
    private SentimentLabel label;

    @Column(name = "confidence", nullable = false, precision = 6, scale = 4)
    private BigDecimal confidence;

    @Column(name = "score", precision = 6, scale = 4)
    private BigDecimal score;

    @Column(name = "raw_response", columnDefinition = "LONGTEXT")
    private String rawResponse;

    @Column(name = "analyzed_at", nullable = false)
    private LocalDateTime analyzedAt = LocalDateTime.now();

    @Column(name = "is_latest", nullable = false)
    private boolean latest = true;
}
