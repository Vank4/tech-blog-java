package com.techblog.domain.rating.repository;

import com.techblog.domain.rating.model.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRatingRepository extends JpaRepository<ProductRating, Long> {
    Optional<ProductRating> findByUserIdAndProductId(Long userId, Long productId);
}