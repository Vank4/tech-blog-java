package com.techblog.domain.product.repository;

import com.techblog.domain.product.model.ProductSpec;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSpecRepository extends JpaRepository<ProductSpec, Long> {

    List<ProductSpec> findByProductIdOrderByDisplayOrderAsc(Long productId);
}
