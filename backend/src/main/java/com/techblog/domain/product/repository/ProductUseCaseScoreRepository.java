package com.techblog.domain.product.repository;

import com.techblog.domain.product.model.ProductUseCaseScore;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUseCaseScoreRepository extends JpaRepository<ProductUseCaseScore, Long> {

    List<ProductUseCaseScore> findByProductId(Long productId);
}
