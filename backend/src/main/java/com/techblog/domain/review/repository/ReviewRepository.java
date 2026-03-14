package com.techblog.domain.review.repository;

import com.techblog.domain.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}