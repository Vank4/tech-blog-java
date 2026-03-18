package com.techblog.domain.review.repository;

import com.techblog.common.enums.ContentStatus;
import com.techblog.domain.review.model.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findBySlug(String slug);

    List<Review> findByProductIdAndStatusOrderByPublishedAtDesc(Long productId, ContentStatus status);
}
