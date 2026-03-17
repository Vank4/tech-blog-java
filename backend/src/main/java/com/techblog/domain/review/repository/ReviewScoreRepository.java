package com.techblog.domain.review.repository;

import com.techblog.domain.review.model.ReviewScore;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewScoreRepository extends JpaRepository<ReviewScore, Long> {

    List<ReviewScore> findByReviewIdOrderByDisplayOrderAsc(Long reviewId);
}
