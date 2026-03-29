package com.techblog.domain.product.service;

import com.techblog.common.enums.ProductStatus;
import com.techblog.common.exception.ResourceNotFoundException;
import com.techblog.domain.product.dto.ProductDiscussionResponse;
import com.techblog.domain.category.model.Category;
import com.techblog.domain.category.repository.CategoryRepository;
import com.techblog.domain.product.dto.CreateProductRequest;
import com.techblog.domain.product.dto.ProductImageRequest;
import com.techblog.domain.product.dto.ProductImageResponse;
import com.techblog.domain.product.dto.ProductResponse;
import com.techblog.domain.product.dto.ProductSpecRequest;
import com.techblog.domain.product.dto.ProductSpecResponse;
import com.techblog.domain.product.dto.ProductStatusUpdateRequest;
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
import jakarta.persistence.criteria.JoinType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductSpecRepository productSpecRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getPublicProducts(String categorySlug, String brand, String sort) {
        Specification<Product> specification = isPublished();

        if (StringUtils.hasText(categorySlug)) {
            specification = specification.and(hasCategorySlug(categorySlug));
        }

        if (StringUtils.hasText(brand)) {
            specification = specification.and(hasBrand(brand));
        }

        return productRepository.findAll(specification, resolveSort(sort))
                .stream()
                .map(product -> mapToResponse(product, false))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getPublicProductBySlug(String slug) {
        Product product = productRepository.findBySlugAndStatus(slug, ProductStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return mapToResponse(product, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getPublicProducts(List<String> categorySlugs, List<String> brands, BigDecimal maxPrice, Integer minRating, String sort) {
        return productRepository.findAll().stream()
                .filter(product -> product.getStatus() == ProductStatus.PUBLISHED)
                .filter(product -> categorySlugs == null || categorySlugs.isEmpty()
                        || (product.getCategory() != null && categorySlugs.stream()
                        .anyMatch(slug -> slug != null && slug.equalsIgnoreCase(product.getCategory().getSlug()))))
                .filter(product -> brands == null || brands.isEmpty()
                        || brands.stream().anyMatch(brand -> brand != null && brand.equalsIgnoreCase(product.getBrand())))
                .filter(product -> maxPrice == null || product.getPrice() == null || product.getPrice().compareTo(maxPrice) <= 0)
                .filter(product -> minRating == null || product.getRatingAverage() == null
                        || product.getRatingAverage().compareTo(BigDecimal.valueOf(minRating)) >= 0)
                .sorted(resolveSort(sort).getOrderFor("publishedAt") != null
                        ? Comparator.comparing(Product::getPublishedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        : Comparator.comparing(Product::getId).reversed())
                .map(product -> mapToResponse(product, true))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getPublicMaxPrice() {
        return productRepository.findAll().stream()
                .filter(product -> product.getStatus() == ProductStatus.PUBLISHED)
                .map(Product::getPrice)
                .filter(price -> price != null)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ONE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getDistinctBrands() {
        return productRepository.findAll().stream()
                .map(Product::getBrand)
                .filter(StringUtils::hasText)
                .map(String::trim)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDiscussionResponse> getPublicProductDiscussions(Long productId) {
        return reviewRepository.findByProductIdAndStatusOrderByPublishedAtDesc(productId, com.techblog.common.enums.ContentStatus.PUBLISHED)
                .stream()
                .map(this::mapDiscussionResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProductsForAdmin(String q, Long categoryId, String status) {
        return productRepository.findAll().stream()
                .filter(product -> !StringUtils.hasText(q)
                        || containsIgnoreCase(product.getName(), q)
                        || containsIgnoreCase(product.getBrand(), q)
                        || containsIgnoreCase(product.getSlug(), q))
                .filter(product -> categoryId == null
                        || (product.getCategory() != null && categoryId.equals(product.getCategory().getId())))
                .filter(product -> !StringUtils.hasText(status)
                        || product.getStatus().name().equalsIgnoreCase(status))
                .sorted(Comparator.comparing(Product::getId).reversed())
                .map(product -> mapToResponse(product, true))
                .toList();
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest request, String actorEmail) {
        validateSlugForCreate(request.getSlug());

        User actor = userRepository.findByEmail(actorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = new Product();
        applyRequest(product, request);
        product.setCreatedBy(actor);
        product.setStatus(ProductStatus.DRAFT);
        product.setPublishedAt(null);

        return mapToResponse(productRepository.save(product), true);
    }

    @Override
    public ProductResponse updateProduct(Long id, CreateProductRequest request) {
        Product product = getProductById(id);

        if (product.getStatus() == ProductStatus.DELETED) {
            throw new IllegalStateException("Deleted products must be restored before updating");
        }

        validateSlugForUpdate(product.getId(), request.getSlug());
        applyRequest(product, request);
        return mapToResponse(productRepository.save(product), true);
    }

    @Override
    public ProductResponse updateProductStatus(Long id, ProductStatusUpdateRequest request) {
        Product product = getProductById(id);

        if (product.getStatus() == ProductStatus.DELETED) {
            throw new IllegalStateException("Deleted products cannot be published or hidden");
        }

        ProductStatus nextStatus = request.getStatus();
        if (nextStatus != ProductStatus.PUBLISHED && nextStatus != ProductStatus.HIDDEN) {
            throw new IllegalArgumentException("Only PUBLISHED or HIDDEN are supported in this endpoint");
        }

        product.setStatus(nextStatus);
        if (nextStatus == ProductStatus.PUBLISHED) {
            product.setPublishedAt(LocalDateTime.now());
        } else {
            product.setPublishedAt(null);
        }

        return mapToResponse(productRepository.save(product), true);
    }

    @Override
    public void softDeleteProduct(Long id) {
        Product product = getProductById(id);
        product.setStatus(ProductStatus.DELETED);
        product.setPublishedAt(null);
        productRepository.save(product);
    }

    @Override
    public ProductResponse restoreProduct(Long id) {
        Product product = getProductById(id);

        if (product.getStatus() != ProductStatus.DELETED) {
            throw new IllegalStateException("Only deleted products can be restored");
        }

        product.setStatus(ProductStatus.DRAFT);
        product.setPublishedAt(null);
        return mapToResponse(productRepository.save(product), true);
    }

    @Override
    public ProductImageResponse addImage(Long productId, ProductImageRequest request) {
        Product product = getProductById(productId);
        List<ProductImage> existingImages = productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId);

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setImageUrl(request.getImageUrl().trim());
        image.setAltText(normalizeNullable(request.getAltText()));
        image.setDisplayOrder(request.getDisplayOrder());

        boolean shouldSetMain = request.isPrimary() || existingImages.isEmpty();
        if (shouldSetMain) {
            clearPrimaryFlag(existingImages);
        }

        image.setPrimary(shouldSetMain);
        return mapImageResponse(productImageRepository.save(image));
    }

    @Override
    public void removeImage(Long productId, Long imageId) {
        getProductById(productId);
        ProductImage image = productImageRepository.findByIdAndProductId(imageId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product image not found"));
        productImageRepository.delete(image);
    }

    @Override
    public ProductImageResponse setMainImage(Long productId, Long imageId) {
        getProductById(productId);

        List<ProductImage> images = productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId);
        ProductImage targetImage = images.stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product image not found"));

        clearPrimaryFlag(images);
        targetImage.setPrimary(true);
        return mapImageResponse(productImageRepository.save(targetImage));
    }

    @Override
    public ProductSpecResponse addSpec(Long productId, ProductSpecRequest request) {
        Product product = getProductById(productId);

        ProductSpec spec = new ProductSpec();
        spec.setProduct(product);
        spec.setSpecKey(request.getSpecKey().trim());
        spec.setSpecValue(request.getSpecValue().trim());
        spec.setUnit(normalizeNullable(request.getUnit()));
        spec.setDisplayOrder(request.getDisplayOrder());

        return mapSpecResponse(productSpecRepository.save(spec));
    }

    @Override
    public ProductSpecResponse updateSpec(Long productId, Long specId, ProductSpecRequest request) {
        getProductById(productId);
        ProductSpec spec = productSpecRepository.findByIdAndProductId(specId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product spec not found"));

        spec.setSpecKey(request.getSpecKey().trim());
        spec.setSpecValue(request.getSpecValue().trim());
        spec.setUnit(normalizeNullable(request.getUnit()));
        spec.setDisplayOrder(request.getDisplayOrder());

        return mapSpecResponse(productSpecRepository.save(spec));
    }

    @Override
    public void removeSpec(Long productId, Long specId) {
        getProductById(productId);
        ProductSpec spec = productSpecRepository.findByIdAndProductId(specId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product spec not found"));
        productSpecRepository.delete(spec);
    }

    private void applyRequest(Product product, CreateProductRequest request) {
        product.setName(request.getName().trim());
        product.setSlug(request.getSlug().trim());
        product.setBrand(request.getBrand().trim());
        product.setModelCode(normalizeNullable(request.getModel()));
        product.setShortDescription(request.getShortDescription().trim());
        product.setDescription(request.getDescription().trim());
        product.setPrice(request.getPrice());
        product.setCurrency(request.getCurrency().trim().toUpperCase());
        product.setAllowComments(request.isAllowComments());
        product.setCategory(resolveCategory(request.getCategoryId()));
    }

    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private void validateSlugForCreate(String slug) {
        if (productRepository.existsBySlug(slug.trim())) {
            throw new IllegalArgumentException("Slug already exists");
        }
    }

    private void validateSlugForUpdate(Long productId, String slug) {
        productRepository.findBySlug(slug.trim())
                .filter(existing -> !existing.getId().equals(productId))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Slug already exists");
                });
    }

    private Specification<Product> isPublished() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), ProductStatus.PUBLISHED);
    }

    private Specification<Product> hasCategorySlug(String categorySlug) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.lower(root.join("category", JoinType.LEFT).get("slug")),
                categorySlug.trim().toLowerCase()
        );
    }

    private Specification<Product> hasBrand(String brand) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.lower(root.get("brand")),
                brand.trim().toLowerCase()
        );
    }

    private Sort resolveSort(String sort) {
        if ("rating-asc".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Order.asc("ratingAverage"), Sort.Order.asc("id"));
        }

        if ("latest".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Order.desc("publishedAt"), Sort.Order.desc("id"));
        }

        return Sort.by(Sort.Order.desc("ratingAverage"), Sort.Order.desc("ratingCount"), Sort.Order.desc("id"));
    }

    private ProductResponse mapToResponse(Product product, boolean includeAssets) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSlug(product.getSlug());
        response.setBrand(product.getBrand());
        response.setModel(product.getModelCode());
        response.setShortDescription(product.getShortDescription());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCurrency(product.getCurrency());
        response.setStatus(product.getStatus());
        response.setRatingAverage(product.getRatingAverage());
        response.setRatingCount(product.getRatingCount());
        response.setAllowComments(product.isAllowComments());
        response.setPublishedAt(product.getPublishedAt());

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
            response.setCategorySlug(product.getCategory().getSlug());
        }

        List<ProductImage> images = productImageRepository.findByProductIdOrderByDisplayOrderAsc(product.getId());
        response.setThumbnailUrl(resolveThumbnail(images));

        if (includeAssets) {
            response.setImages(images.stream()
                    .map(this::mapImageResponse)
                    .toList());
            response.setSpecs(productSpecRepository.findByProductIdOrderByDisplayOrderAsc(product.getId())
                    .stream()
                    .map(this::mapSpecResponse)
                    .toList());
        }

        return response;
    }

    private String resolveThumbnail(List<ProductImage> images) {
        return images.stream()
                .sorted(Comparator.comparing(ProductImage::isPrimary).reversed()
                        .thenComparing(ProductImage::getDisplayOrder)
                        .thenComparing(ProductImage::getId))
                .map(ProductImage::getImageUrl)
                .findFirst()
                .orElse(null);
    }

    private ProductImageResponse mapImageResponse(ProductImage image) {
        ProductImageResponse response = new ProductImageResponse();
        response.setId(image.getId());
        response.setImageUrl(image.getImageUrl());
        response.setAltText(image.getAltText());
        response.setPrimary(image.isPrimary());
        response.setDisplayOrder(image.getDisplayOrder());
        return response;
    }

    private ProductSpecResponse mapSpecResponse(ProductSpec spec) {
        ProductSpecResponse response = new ProductSpecResponse();
        response.setId(spec.getId());
        response.setSpecKey(spec.getSpecKey());
        response.setSpecValue(spec.getSpecValue());
        response.setUnit(spec.getUnit());
        response.setDisplayOrder(spec.getDisplayOrder());
        return response;
    }

    private ProductDiscussionResponse mapDiscussionResponse(Review review) {
        ProductDiscussionResponse response = new ProductDiscussionResponse();
        response.setId(review.getId());
        response.setHeadline(review.getTitle());
        response.setAuthorName(review.getAuthor() != null ? review.getAuthor().getDisplayName() : null);
        response.setAuthorMeta(review.getAuthor() != null ? review.getAuthor().getEmail() : null);
        response.setContent(review.getSummary() != null ? review.getSummary() : review.getContent());
        response.setCreatedAt(review.getPublishedAt() != null ? review.getPublishedAt() : review.getCreatedAt());
        response.setHelpfulCount(0);
        response.setReplyCount(0);
        response.setStarCount(review.getOverallScore() != null ? review.getOverallScore().intValue() : null);
        response.setBadgeLabel("Review");
        response.setVerified(true);
        return response;
    }

    private boolean containsIgnoreCase(String source, String keyword) {
        return StringUtils.hasText(source) && StringUtils.hasText(keyword)
                && source.toLowerCase().contains(keyword.trim().toLowerCase());
    }

    private void clearPrimaryFlag(List<ProductImage> images) {
        for (ProductImage image : images) {
            if (image.isPrimary()) {
                image.setPrimary(false);
            }
        }
        if (!images.isEmpty()) {
            productImageRepository.saveAll(images);
        }
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value.trim();
    }
}
