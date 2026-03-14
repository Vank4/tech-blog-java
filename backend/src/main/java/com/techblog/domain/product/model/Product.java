package com.techblog.domain.product.model;

import com.techblog.common.audit.BaseAuditEntity;
import com.techblog.common.enums.ProductStatus;
import com.techblog.domain.category.model.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_products_slug", columnNames = "slug")
        }
)
public class Product extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String slug;

    @Column(length = 120)
    private String brand;

    @Column(length = 120)
    private String model;

    @Column(name = "short_desc", columnDefinition = "TEXT")
    private String shortDesc;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.DRAFT;

    @Column(precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "thumbnail_url", length = 255)
    private String thumbnailUrl;
}