package com.techblog.domain.product.service;

import com.techblog.common.enums.ProductStatus;
import com.techblog.common.exception.ResourceNotFoundException;
import com.techblog.domain.category.model.Category;
import com.techblog.domain.category.repository.CategoryRepository;
import com.techblog.domain.product.dto.CreateProductRequest;
import com.techblog.domain.product.dto.ProductResponse;
import com.techblog.domain.product.dto.ProductStatusUpdateRequest;
import com.techblog.domain.product.model.Product;
import com.techblog.domain.product.repository.ProductRepository;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import jakarta.persistence.criteria.JoinType;
import java.time.LocalDateTime;
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
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getPublicProductBySlug(String slug) {
        Product product = productRepository.findBySlugAndStatus(slug, ProductStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return mapToResponse(product);
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

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse updateProduct(Long id, CreateProductRequest request) {
        Product product = getProductById(id);

        if (product.getStatus() == ProductStatus.DELETED) {
            throw new IllegalStateException("Deleted products must be restored before updating");
        }

        validateSlugForUpdate(product.getId(), request.getSlug());
        applyRequest(product, request);
        return mapToResponse(productRepository.save(product));
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
        }

        return mapToResponse(productRepository.save(product));
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
        return mapToResponse(productRepository.save(product));
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

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value.trim();
    }

    private ProductResponse mapToResponse(Product product) {
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

        return response;
    }
}