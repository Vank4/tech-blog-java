package com.techblog.domain.product.repository;

import com.techblog.common.enums.ProductStatus;
import com.techblog.domain.product.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySlug(String slug);

    List<Product> findByStatusOrderByPublishedAtDesc(ProductStatus status);
}
