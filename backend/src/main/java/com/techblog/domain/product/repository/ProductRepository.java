package com.techblog.domain.product.repository;

import com.techblog.common.enums.ProductStatus;
import com.techblog.domain.product.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySlugAndStatus(String slug, ProductStatus status);

    List<Product> findByStatusOrderByPublishedAtDesc(ProductStatus status);

    boolean existsBySlug(String slug);
}