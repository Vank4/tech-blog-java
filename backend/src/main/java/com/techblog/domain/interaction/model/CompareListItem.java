package com.techblog.domain.interaction.model;

import com.techblog.common.audit.Auditable;
import com.techblog.domain.product.model.Product;
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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "compare_list_items", uniqueConstraints = {
        @UniqueConstraint(name = "uk_compare_list_items_list_product", columnNames = { "compare_list_id", "product_id" }),
        @UniqueConstraint(name = "uk_compare_list_items_list_position", columnNames = { "compare_list_id", "position" })
})
public class CompareListItem extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compare_list_id", nullable = false)
    private CompareList compareList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "position", nullable = false)
    private int position;
}
