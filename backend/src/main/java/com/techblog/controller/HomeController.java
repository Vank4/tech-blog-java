package com.techblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home/index";
    }

    @GetMapping("/post/{slug}")
    public String postDetail(@PathVariable String slug) {
        return "Home/detail";
    }

    // ==========================================
    // CÁC ĐƯỜNG DẪN CHO ADMIN
    // ==========================================
    @GetMapping("/admin/posts")
    public String adminPostManager() {
        return "Admin/post-management";
    }

    @GetMapping("/admin/categories")
    public String adminCategoryManager() {
        return "Admin/category-management";
    }

    @GetMapping("/admin/tags")
    public String adminTagManager() {
        return "Admin/tag-management";
    }

    // ==========================================
    // CÁC ĐƯỜNG DẪN CHO TÁC GIẢ (AUTHOR) -
    // ==========================================

    // 1. Trang danh sách bài viết cá nhân của tác giả
    @GetMapping("/author/posts")
    public String authorPostManager() {
        return "Author/post-list"; // Trỏ đến templates/Author/post-list.html
    }

    // 2. Trang soạn thảo (Dùng chung cho cả Tạo mới và Chỉnh sửa)
    @GetMapping("/author/posts/editor")
    public String authorEditor() {
        return "Author/editor"; // Trỏ đến templates/Author/editor.html
    }
}