package com.techblog.domain.post.controller;

import com.techblog.common.enums.ContentStatus;
import com.techblog.common.response.ApiResponse;
import com.techblog.domain.post.dto.*;
import com.techblog.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // ==========================================
    // 1. CÁC API DÀNH CHO TÁC GIẢ (AUTHOR)
    // ==========================================

    // 1.1 API Tạo bản nháp
    @PostMapping
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ApiResponse<PostResponse>> createDraft(
            @Valid @RequestBody CreatePostRequest request,
            Principal principal) {
        // Lấy trực tiếp từ Principal, nếu chưa login Spring Security sẽ tự chặn ở Filter
        String userEmail = principal.getName();
        PostResponse response = postService.createDraft(request, userEmail);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tạo bản nháp thành công", response));
    }

    // 1.2 API Gửi duyệt (DRAFT -> PENDING)
    @PatchMapping("/{id}/submit")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ApiResponse<Void>> submitPost(@PathVariable Long id, Principal principal) {
        String userEmail = principal.getName();
        postService.submitPost(id, userEmail);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã gửi bài viết để chờ duyệt", null));
    }

    // 1.3 API Sửa bài viết
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request,
            Principal principal) {
        String userEmail = principal.getName();
        PostResponse response = postService.updatePost(id, request, userEmail);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật bài viết thành công", response));
    }

    // 1.4 API Xóa bài viết
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id, Principal principal) {
        String userEmail = principal.getName();
        postService.deletePost(id, userEmail);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã xóa bài viết thành công", null));
    }

    // 1.5 API Lấy chi tiết để sửa (Cho cả Author và Admin)
    @GetMapping("/{id}/admin")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        PostResponse response = postService.getPostById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy dữ liệu bài viết thành công", response));
    }

    // ==========================================
    // 2. CÁC API DÀNH CHO ADMIN KIỂM DUYỆT
    // ==========================================

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> approvePost(@PathVariable Long id, Principal principal) {
        String adminEmail = principal.getName();
        postService.approvePost(id, adminEmail);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã phê duyệt bài viết", null));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> rejectPost(
            @PathVariable Long id,
            @RequestParam String reason,
            Principal principal) {
        String adminEmail = principal.getName();
        postService.rejectPost(id, adminEmail, reason);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã từ chối bài viết", null));
    }

    @PatchMapping("/{id}/hide")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> hidePost(
            @PathVariable Long id,
            @RequestParam String reason,
            Principal principal) {
        String adminEmail = principal.getName();
        postService.hidePost(id, adminEmail, reason);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã ẩn bài viết thành công", null));
    }

    @PutMapping("/{id}/featured")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> setFeatured(
            @PathVariable Long id,
            @RequestBody FeaturedRequest request) {
        PostResponse response = postService.setFeatured(id, request.isFeatured(), request.getPriority());
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật trạng thái nổi bật thành công", response));
    }

    @GetMapping("/moderation-logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ModerationLogResponse>>> getModerationLogs() {
        List<ModerationLogResponse> logs = postService.getAllModerationLogs();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy nhật ký thành công", logs));
    }

    // ==========================================
    // 3. CÁC API PUBLIC (CHO KHÁCH ĐỌC)
    // ==========================================

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ContentStatus status,
            @RequestParam(required = false) String title) {

        Page<PostResponse> responsePage = postService.getPosts(status, title, page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách bài viết thành công", responsePage));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostBySlug(@PathVariable String slug) {
        PostResponse response = postService.getPostBySlug(slug);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy chi tiết bài viết thành công", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostResponse> results = postService.searchPublicPosts(keyword, tagId, page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tìm kiếm thành công", results));
    }
}