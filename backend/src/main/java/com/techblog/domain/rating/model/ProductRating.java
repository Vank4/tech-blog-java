package com.techblog.domain.rating.model;

import com.techblog.common.audit.BaseAuditEntity;
import com.techblog.domain.product.model.Product;
import com.techblog.domain.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "product_ratings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_ratings_user_product",
                        columnNames = {"user_id", "product_id"}
                )
        }
)
public class ProductRating extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer stars;
}