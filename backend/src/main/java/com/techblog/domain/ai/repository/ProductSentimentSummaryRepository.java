package com.techblog.domain.ai.repository;

import com.techblog.domain.ai.model.ProductSentimentSummary;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSentimentSummaryRepository extends JpaRepository<ProductSentimentSummary, Long> {

    Optional<ProductSentimentSummary> findByProductId(Long productId);
}
