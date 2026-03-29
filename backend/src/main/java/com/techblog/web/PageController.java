package com.techblog.web;

import com.techblog.common.exception.ResourceNotFoundException;
import com.techblog.domain.category.dto.CategoryResponse;
import com.techblog.domain.category.service.CategoryService;
import com.techblog.domain.product.dto.ProductDiscussionResponse;
import com.techblog.domain.product.dto.ProductResponse;
import com.techblog.domain.product.dto.ProductSpecResponse;
import com.techblog.domain.product.service.ProductService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String products(
            @RequestParam(name = "category", required = false) List<String> categorySlugs,
            @RequestParam(name = "brand", required = false) List<String> brands,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false, defaultValue = "0") Integer minRating,
            @RequestParam(defaultValue = "rating") String sort,
            Model model) {

        BigDecimal selectedMaxPrice = maxPrice != null ? BigDecimal.valueOf(maxPrice) : null;
        List<ProductResponse> products = productService.getPublicProducts(categorySlugs, brands, selectedMaxPrice, minRating, sort);
        List<CategoryResponse> categories = categoryService.getAllCategories();
        List<String> allBrands = productService.getDistinctBrands();
        long priceCeiling = Math.max(1L, productService.getPublicMaxPrice().longValue());

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", allBrands);
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("sort", sort);
        model.addAttribute("selectedCategorySlugs", normalizeValues(categorySlugs));
        model.addAttribute("selectedBrands", normalizeValues(brands));
        model.addAttribute("priceCeiling", priceCeiling);
        model.addAttribute("selectedMaxPrice", selectedMaxPrice != null ? selectedMaxPrice.longValue() : priceCeiling);
        model.addAttribute("selectedMinRating", Math.max(0, minRating != null ? minRating : 0));
        return "products/listing";
    }

    @GetMapping("/products/{slug}")
    public String productDetail(@PathVariable String slug, Model model) {
        ProductResponse product = productService.getPublicProductBySlug(slug);
        List<SpecGroupView> specGroups = buildSpecGroups(product.getSpecs());
        List<ProductDiscussionResponse> discussionCards = productService.getPublicProductDiscussions(product.getId());

        model.addAttribute("product", product);
        model.addAttribute("specGroups", specGroups);
        model.addAttribute("discussionCards", discussionCards);
        model.addAttribute("discussionCount", discussionCards.size());
        return "products/detail";
    }

    @GetMapping("/compare")
    public String compare(
            @RequestParam(required = false) List<String> slugs,
            Model model) {

        List<String> selectedSlugs = normalizeValues(slugs).stream().limit(3).toList();
        List<ProductResponse> comparisonProducts = selectedSlugs.stream()
                .map(this::findPublicProduct)
                .filter(Objects::nonNull)
                .toList();

        model.addAttribute("comparisonProducts", comparisonProducts);
        model.addAttribute("comparisonSpecKeys", buildComparisonSpecKeys(comparisonProducts));
        model.addAttribute("selectedCompareSlugs", comparisonProducts.stream().map(ProductResponse::getSlug).toList());
        return "products/comparison";
    }

    private ProductResponse findPublicProduct(String slug) {
        try {
            return productService.getPublicProductBySlug(slug);
        } catch (ResourceNotFoundException ignored) {
            return null;
        }
    }

    private List<String> normalizeValues(List<String> values) {
        if (values == null || values.isEmpty()) {
            return List.of();
        }

        List<String> normalized = new ArrayList<>();
        for (String value : values) {
            if (!StringUtils.hasText(value)) {
                continue;
            }
            String trimmed = value.trim();
            if (!normalized.contains(trimmed)) {
                normalized.add(trimmed);
            }
        }
        return normalized;
    }

    private List<SpecGroupView> buildSpecGroups(List<ProductSpecResponse> specs) {
        if (specs == null || specs.isEmpty()) {
            return List.of();
        }

        Map<String, List<ProductSpecResponse>> grouped = new LinkedHashMap<>();
        grouped.put("Core Performance", new ArrayList<>());
        grouped.put("Display & Visuals", new ArrayList<>());
        grouped.put("Connectivity & I/O", new ArrayList<>());
        grouped.put("Additional Details", new ArrayList<>());

        for (ProductSpecResponse spec : specs) {
            grouped.get(resolveSpecGroupName(spec.getSpecKey())).add(spec);
        }

        return grouped.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> new SpecGroupView(entry.getKey(), entry.getValue()))
                .toList();
    }

    private String resolveSpecGroupName(String specKey) {
        String normalizedKey = specKey == null ? "" : specKey.toLowerCase();

        if (normalizedKey.matches(".*(processor|cpu|chip|chipset|graphics|gpu|memory|ram|storage|ssd|benchmark|performance).*")) {
            return "Core Performance";
        }

        if (normalizedKey.matches(".*(display|screen|panel|resolution|refresh|camera|brightness|contrast|color).*")) {
            return "Display & Visuals";
        }

        if (normalizedKey.matches(".*(port|connect|io|i/o|wifi|bluetooth|network|usb|thunderbolt|hdmi|battery|charging|wireless).*")) {
            return "Connectivity & I/O";
        }

        return "Additional Details";
    }

    private List<String> buildComparisonSpecKeys(List<ProductResponse> comparisonProducts) {
        LinkedHashSet<String> keys = new LinkedHashSet<>();
        for (ProductResponse product : comparisonProducts) {
            if (product.getSpecs() == null) {
                continue;
            }
            for (ProductSpecResponse spec : product.getSpecs()) {
                if (StringUtils.hasText(spec.getSpecKey())) {
                    keys.add(spec.getSpecKey());
                }
            }
        }
        return List.copyOf(keys);
    }

    public static final class SpecGroupView {
        private final String title;
        private final List<ProductSpecResponse> specs;

        public SpecGroupView(String title, List<ProductSpecResponse> specs) {
            this.title = title;
            this.specs = specs;
        }

        public String getTitle() {
            return title;
        }

        public List<ProductSpecResponse> getSpecs() {
            return specs;
        }
    }
}
