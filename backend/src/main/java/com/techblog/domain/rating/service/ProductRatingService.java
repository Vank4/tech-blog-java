package com.techblog.domain.rating.service;

import com.techblog.domain.rating.dto.ProductRatingSummaryResponse;

public interface ProductRatingService {

    ProductRatingSummaryResponse upsertRating(Long productId, int rating, String actorEmail);

    ProductRatingSummaryResponse getRatingSummary(Long productId, String actorEmail);
}