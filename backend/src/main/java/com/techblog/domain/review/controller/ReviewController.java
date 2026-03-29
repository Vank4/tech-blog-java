package com.techblog.domain.review.controller;

import com.techblog.common.response.ApiResponse;
import com.techblog.domain.review.dto.CreateReviewRequest;
import com.techblog.domain.review.dto.ReviewResponse;
import com.techblog.domain.review.dto.UpdateReviewRequest;
import com.techblog.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ==========================================
    // 0. CÁC API CÔNG KHAI (PUBLIC)
    // ==========================================

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getLatestPublishedReviews(
            @RequestParam(defaultValue = "6") int limit) {
        List<ReviewResponse> reviews = reviewService.getLatestPublishedReviews(limit);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách review mới nhất thành công", reviews));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewBySlug(@PathVariable String slug) {
        ReviewResponse response = reviewService.getReviewBySlug(slug);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy dữ liệu review thành công", response));
    }

    // ==========================================
    // 1. CÁC API DÀNH CHO TÁC GIẢ (AUTHOR)
    // ==========================================

    @PostMapping
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<ReviewResponse>> createDraft(
            @Valid @RequestBody CreateReviewRequest request,
            Principal principal) {
        String userEmail = principal.getName();
        ReviewResponse response = reviewService.createDraft(request, userEmail);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tạo bản nháp review thành công", response));
    }

    @PatchMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> submitReview(@PathVariable Long id, Principal principal) {
        String userEmail = principal.getName();
        reviewService.submitReview(id, userEmail);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã gửi review để chờ duyệt", null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReviewRequest request,
            Principal principal) {
        String userEmail = principal.getName();
        ReviewResponse response = reviewService.updateReview(id, request, userEmail);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật review thành công", response));
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getMyReviews(Principal principal) {
        String userEmail = principal.getName();
        List<ReviewResponse> reviews = reviewService.getAuthorReviews(userEmail);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách review của tôi thành công", reviews));
    }

    // ==========================================
    // 2. CÁC API DÀNH CHO ADMIN KIỂM DUYỆT
    // ==========================================

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAllReviewsForAdmin() {
        List<ReviewResponse> reviews = reviewService.getAllReviewsForAdmin();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy toàn bộ danh sách review thành công", reviews));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getPendingReviews() {
        List<ReviewResponse> reviews = reviewService.getPendingReviews();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách review chờ duyệt thành công", reviews));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> approveReview(@PathVariable Long id, Principal principal) {
        String adminEmail = principal.getName();
        reviewService.approveReview(id, adminEmail);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã phê duyệt review", null));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> rejectReview(
            @PathVariable Long id,
            @RequestParam String reason,
            Principal principal) {
        String adminEmail = principal.getName();
        reviewService.rejectReview(id, adminEmail, reason);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã từ chối review", null));
    }

    @PatchMapping("/{id}/hide")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> hideReview(
            @PathVariable Long id,
            @RequestParam String reason,
            Principal principal) {
        String adminEmail = principal.getName();
        reviewService.hideReview(id, adminEmail, reason);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã ẩn review thành công", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewById(@PathVariable Long id) {
        ReviewResponse response = reviewService.getReviewById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy dữ liệu review thành công", response));
    }
}
