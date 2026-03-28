package com.techblog.domain.rating.repository;

import com.techblog.domain.rating.model.ProductRating;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRatingRepository extends JpaRepository<ProductRating, Long> {

    Optional<ProductRating> findByUserIdAndProductId(Long userId, Long productId);

    List<ProductRating> findByProductId(Long productId);
}