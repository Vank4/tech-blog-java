package com.techblog.domain.ai.model;

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
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ai_settings", uniqueConstraints = {
        @UniqueConstraint(name = "uk_ai_settings_name", columnNames = "name")
})
public class AiSetting extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "min_comments_for_conclusion", nullable = false)
    private int minCommentsForConclusion;

    @Column(name = "good_threshold_percent", nullable = false, precision = 6, scale = 2)
    private BigDecimal goodThresholdPercent;

    @Column(name = "bad_threshold_percent", nullable = false, precision = 6, scale = 2)
    private BigDecimal badThresholdPercent;

    @Column(name = "auto_approve_enabled", nullable = false)
    private boolean autoApproveEnabled;

    @Column(name = "auto_approve_min_confidence", nullable = false, precision = 6, scale = 4)
    private BigDecimal autoApproveMinConfidence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "active_model_id")
    private MlModel activeModel;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
