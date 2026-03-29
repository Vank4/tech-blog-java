package com.techblog.web;

import com.techblog.domain.category.dto.CategoryResponse;
import com.techblog.domain.category.service.CategoryService;
import com.techblog.domain.product.dto.ProductResponse;
import com.techblog.domain.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/products")
    public String products(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String status,
            Model model) {

        List<ProductResponse> products = productService.getAllProductsForAdmin(q, category, status);
        List<CategoryResponse> categories = categoryService.getAllCategories();
        List<String> allBrands = productService.getDistinctBrands();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("allBrands", allBrands);
        model.addAttribute("query", q);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("activePage", "products");
        return "admin/products";
    }
}
