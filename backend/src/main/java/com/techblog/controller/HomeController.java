package com.techblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Home/index";
    }

    @GetMapping("/post/{slug}")
    public String postDetail(@PathVariable String slug) {
        return "Home/detail";
    }

    @GetMapping("/review/{slug}")
    public String reviewDetail(@PathVariable String slug) {
        return "Home/review-detail";
    }

    // ==========================================
    // CÁC ĐƯỜNG DẪN CHO ADMIN
    // ==========================================
    @GetMapping("/admin/posts")
    public String adminPostManager() {
        return "admin/post-management";
    }

    @GetMapping("/admin/categories")
    public String adminCategoryManager() {
        return "admin/category-management";
    }

    @GetMapping("/admin/tags")
    public String adminTagManager() {
        return "admin/tag-management";
    }

    @GetMapping("/admin/comments")
    public String adminCommentManager() {
        return "admin/comment-management";
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
        return "Author/editor";
    }

    // 3. Trang danh sách review cá nhân
    @GetMapping("/author/reviews")
    public String authorReviewManager() {
        return "Author/review-list";
    }

    // 4. Trang soạn thảo review
    @GetMapping("/author/reviews/editor")
    public String authorReviewEditor() {
        return "Author/review-editor";
    }

    // ==========================================
    // TRANG KIỂM DUYỆT (ADMIN)
    // ==========================================
    @GetMapping("/admin/reviews/moderation")
    public String adminReviewModeration() {
        return "admin/review-moderation";
    }
}
