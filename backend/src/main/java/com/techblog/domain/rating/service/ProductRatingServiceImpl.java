package com.techblog.domain.rating.service;

import com.techblog.common.enums.ProductStatus;
import com.techblog.common.exception.ResourceNotFoundException;
import com.techblog.domain.product.model.Product;
import com.techblog.domain.product.repository.ProductRepository;
import com.techblog.domain.rating.dto.ProductRatingSummaryResponse;
import com.techblog.domain.rating.model.ProductRating;
import com.techblog.domain.rating.repository.ProductRatingRepository;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductRatingServiceImpl implements ProductRatingService {

    private final ProductRatingRepository productRatingRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public ProductRatingSummaryResponse upsertRating(Long productId, int rating, String actorEmail) {
        Product product = getPublishedProduct(productId);
        User user = getUserByEmail(actorEmail);

        ProductRating productRating = productRatingRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseGet(ProductRating::new);

        productRating.setUser(user);
        productRating.setProduct(product);
        productRating.setRating(rating);
        productRatingRepository.save(productRating);

        refreshProductAggregate(product);
        return buildSummary(product, rating);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductRatingSummaryResponse getRatingSummary(Long productId, String actorEmail) {
        Product product = getPublishedProduct(productId);
        Integer currentUserRating = null;

        if (StringUtils.hasText(actorEmail)) {
            currentUserRating = userRepository.findByEmail(actorEmail)
                    .flatMap(user -> productRatingRepository.findByUserIdAndProductId(user.getId(), productId))
                    .map(ProductRating::getRating)
                    .orElse(null);
        }

        return buildSummary(product, currentUserRating);
    }

    private void refreshProductAggregate(Product product) {
        List<ProductRating> ratings = productRatingRepository.findByProductId(product.getId());

        int ratingCount = ratings.size();
        BigDecimal ratingAverage = BigDecimal.ZERO;

        if (!ratings.isEmpty()) {
            int total = ratings.stream()
                    .mapToInt(ProductRating::getRating)
                    .sum();
            ratingAverage = BigDecimal.valueOf(total)
                    .divide(BigDecimal.valueOf(ratingCount), 2, RoundingMode.HALF_UP);
        }

        product.setRatingCount(ratingCount);
        product.setRatingAverage(ratingAverage);
        productRepository.save(product);
    }

    private Product getPublishedProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStatus() != ProductStatus.PUBLISHED) {
            throw new ResourceNotFoundException("Product not found");
        }

        return product;
    }

    private User getUserByEmail(String actorEmail) {
        return userRepository.findByEmail(actorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private ProductRatingSummaryResponse buildSummary(Product product, Integer currentUserRating) {
        ProductRatingSummaryResponse response = new ProductRatingSummaryResponse();
        response.setProductId(product.getId());
        response.setRatingAverage(product.getRatingAverage());
        response.setRatingCount(product.getRatingCount());
        response.setCurrentUserRating(currentUserRating);
        return response;
    }
}