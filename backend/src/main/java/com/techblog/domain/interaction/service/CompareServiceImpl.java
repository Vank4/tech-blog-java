package com.techblog.domain.interaction.service;

import com.techblog.common.enums.ContentStatus;
import com.techblog.common.enums.ProductStatus;
import com.techblog.common.exception.ResourceNotFoundException;
import com.techblog.domain.ai.model.ProductSentimentSummary;
import com.techblog.domain.ai.repository.ProductSentimentSummaryRepository;
import com.techblog.domain.interaction.dto.CompareItemResponse;
import com.techblog.domain.interaction.dto.CompareProductSpecResponse;
import com.techblog.domain.interaction.dto.CompareResponse;
import com.techblog.domain.interaction.dto.ProductSentimentResponse;
import com.techblog.domain.interaction.model.CompareList;
import com.techblog.domain.interaction.model.CompareListItem;
import com.techblog.domain.interaction.repository.CompareListItemRepository;
import com.techblog.domain.interaction.repository.CompareListRepository;
import com.techblog.domain.product.model.Product;
import com.techblog.domain.product.model.ProductImage;
import com.techblog.domain.product.model.ProductSpec;
import com.techblog.domain.product.repository.ProductImageRepository;
import com.techblog.domain.product.repository.ProductRepository;
import com.techblog.domain.product.repository.ProductSpecRepository;
import com.techblog.domain.review.model.Review;
import com.techblog.domain.review.repository.ReviewRepository;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompareServiceImpl implements CompareService {

    private static final String DEFAULT_COMPARE_LIST_NAME = "default";

    private final CompareListRepository compareListRepository;
    private final CompareListItemRepository compareListItemRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductSpecRepository productSpecRepository;
    private final ReviewRepository reviewRepository;
    private final ProductSentimentSummaryRepository productSentimentSummaryRepository;
    private final UserRepository userRepository;

    @Override
    public CompareResponse getCompare(String actorEmail) {
        CompareList compareList = getOrCreateDefaultCompareList(getUserByEmail(actorEmail));
        List<CompareListItem> items = compareListItemRepository.findByCompareListIdOrderByPositionAsc(compareList.getId());
        return mapCompareResponse(compareList, items);
    }

    @Override
    public CompareResponse addItem(String actorEmail, Long productId) {
        User user = getUserByEmail(actorEmail);
        CompareList compareList = getOrCreateDefaultCompareList(user);
        Product product = getPublishedProduct(productId);

        compareListItemRepository.findByCompareListIdAndProductId(compareList.getId(), productId)
                .orElseGet(() -> {
                    CompareListItem item = new CompareListItem();
                    item.setCompareList(compareList);
                    item.setProduct(product);
                    item.setPosition(compareListItemRepository.findMaxPositionByCompareListId(compareList.getId()) + 1);
                    return compareListItemRepository.save(item);
                });

        List<CompareListItem> items = compareListItemRepository.findByCompareListIdOrderByPositionAsc(compareList.getId());
        return mapCompareResponse(compareList, items);
    }

    @Override
    public void removeItem(String actorEmail, Long productId) {
        CompareList compareList = getOrCreateDefaultCompareList(getUserByEmail(actorEmail));

        CompareListItem item = compareListItemRepository.findByCompareListIdAndProductId(compareList.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Compare item not found"));

        compareListItemRepository.delete(item);
        reindexPositions(compareList.getId());
    }

    private void reindexPositions(Long compareListId) {
        List<CompareListItem> items = compareListItemRepository.findByCompareListIdOrderByPositionAsc(compareListId);
        for (int index = 0; index < items.size(); index++) {
            items.get(index).setPosition(index + 1);
        }
        compareListItemRepository.saveAll(items);
    }

    private CompareList getOrCreateDefaultCompareList(User user) {
        return compareListRepository.findByUserIdAndName(user.getId(), DEFAULT_COMPARE_LIST_NAME)
                .orElseGet(() -> {
                    CompareList compareList = new CompareList();
                    compareList.setUser(user);
                    compareList.setName(DEFAULT_COMPARE_LIST_NAME);
                    compareList.setDescription("Default compare list");
                    return compareListRepository.save(compareList);
                });
    }

    private User getUserByEmail(String actorEmail) {
        return userRepository.findByEmail(actorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Product getPublishedProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStatus() != ProductStatus.PUBLISHED) {
            throw new ResourceNotFoundException("Product not found");
        }

        return product;
    }

    private CompareResponse mapCompareResponse(CompareList compareList, List<CompareListItem> items) {
        CompareResponse response = new CompareResponse();
        response.setListId(compareList.getId());
        response.setName(compareList.getName());
        response.setItems(items.stream().map(this::mapCompareItem).toList());
        return response;
    }

    private CompareItemResponse mapCompareItem(CompareListItem item) {
        Product product = item.getProduct();
        CompareItemResponse response = new CompareItemResponse();
        response.setItemId(item.getId());
        response.setPosition(item.getPosition());
        response.setProductId(product.getId());
        response.setName(product.getName());
        response.setSlug(product.getSlug());
        response.setBrand(product.getBrand());
        response.setModel(product.getModelCode());
        response.setPrice(product.getPrice());
        response.setCurrency(product.getCurrency());
        response.setThumbnailUrl(resolveThumbnailUrl(product.getId()));
        response.setRatingAverage(product.getRatingAverage());
        response.setRatingCount(product.getRatingCount());
        response.setReviewScore(resolveReviewScore(product.getId()));
        response.setSentiment(resolveSentiment(product.getId()));
        response.setSpecs(resolveSpecs(product.getId()));
        return response;
    }

    private String resolveThumbnailUrl(Long productId) {
        List<ProductImage> images = productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId);
        return images.stream()
                .filter(ProductImage::isPrimary)
                .findFirst()
                .or(() -> images.stream().min(Comparator.comparingInt(ProductImage::getDisplayOrder)))
                .map(ProductImage::getImageUrl)
                .orElse(null);
    }

    private List<CompareProductSpecResponse> resolveSpecs(Long productId) {
        return productSpecRepository.findByProductIdOrderByDisplayOrderAsc(productId)
                .stream()
                .map(this::mapSpec)
                .toList();
    }

    private CompareProductSpecResponse mapSpec(ProductSpec spec) {
        CompareProductSpecResponse response = new CompareProductSpecResponse();
        response.setId(spec.getId());
        response.setSpecKey(spec.getSpecKey());
        response.setSpecValue(spec.getSpecValue());
        response.setUnit(spec.getUnit());
        response.setDisplayOrder(spec.getDisplayOrder());
        return response;
    }

    private BigDecimal resolveReviewScore(Long productId) {
        List<Review> reviews = reviewRepository.findByProductIdAndStatusOrderByPublishedAtDesc(productId, ContentStatus.PUBLISHED);
        if (reviews.isEmpty()) {
            return null;
        }

        BigDecimal total = reviews.stream()
                .map(Review::getOverallScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(reviews.size()), 2, RoundingMode.HALF_UP);
    }

    private ProductSentimentResponse resolveSentiment(Long productId) {
        return productSentimentSummaryRepository.findByProductId(productId)
                .map(this::mapSentiment)
                .orElse(null);
    }

    private ProductSentimentResponse mapSentiment(ProductSentimentSummary sentimentSummary) {
        ProductSentimentResponse response = new ProductSentimentResponse();
        response.setTotalComments(sentimentSummary.getTotalComments());
        response.setPositiveCount(sentimentSummary.getPositiveCount());
        response.setNegativeCount(sentimentSummary.getNegativeCount());
        response.setNeutralCount(sentimentSummary.getNeutralCount());
        response.setPositiveRatio(sentimentSummary.getPositiveRatio());
        response.setNegativeRatio(sentimentSummary.getNegativeRatio());
        response.setNeutralRatio(sentimentSummary.getNeutralRatio());
        response.setConclusion(sentimentSummary.getConclusion());
        response.setLastCalculatedAt(sentimentSummary.getLastCalculatedAt());
        return response;
    }
}