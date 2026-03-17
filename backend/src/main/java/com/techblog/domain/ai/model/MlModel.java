package com.techblog.domain.ai.model;

import com.techblog.common.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ml_models", uniqueConstraints = {
        @UniqueConstraint(name = "uk_ml_models_name_version", columnNames = { "name", "version" })
})
public class MlModel extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "version", nullable = false, length = 50)
    private String version;

    @Column(name = "provider", length = 120)
    private String provider;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "deployed_at")
    private LocalDateTime deployedAt;
}
