package com.techblog.domain.post.controller;

import com.techblog.common.enums.ContentStatus;
import com.techblog.common.response.ApiResponse;
import com.techblog.domain.post.dto.CreatePostRequest;
import com.techblog.domain.post.dto.FeaturedRequest;
import com.techblog.domain.post.dto.PostResponse;
import com.techblog.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.techblog.domain.post.dto.UpdatePostRequest;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 1. API Tạo bản nháp (DRAFT) - ĐÃ FIX LỖI NULL TAGS
    @PostMapping
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> createDraft(
            @Valid @RequestBody CreatePostRequest request,
            Principal principal) {

        // Gọi Service và nhận thẳng PostResponse (đã có đủ Tags bên trong)
        PostResponse response = postService.createDraft(request, principal.getName());

        return ResponseEntity.ok(new ApiResponse<>(true, "Tạo bản nháp thành công", response));
    }

    // 2. API Gửi duyệt (DRAFT -> PENDING)
    @PatchMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> submitPost(@PathVariable Long id, Principal principal) {
        postService.submitPost(id, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã gửi bài viết để chờ duyệt", null));
    }

    // 3. API Admin phê duyệt (PENDING -> PUBLISHED)
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> approvePost(@PathVariable Long id, Principal principal) {
        postService.approvePost(id, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã phê duyệt và xuất bản bài viết", null));
    }

    // 4. API Admin từ chối (PENDING -> REJECTED)
    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> rejectPost(
            @PathVariable Long id,
            @RequestParam String reason,
            Principal principal) {
        postService.rejectPost(id, principal.getName(), reason);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã từ chối bài viết", null));
    }

    // 5. API Lấy danh sách bài viết (CÓ PHÂN TRANG)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ContentStatus status) {

        Page<PostResponse> responsePage = postService.getPosts(status, page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách bài viết thành công", responsePage));
    }

    // 6. API Xem chi tiết bài viết (Dành cho mọi người)
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostBySlug(@PathVariable String slug) {
        PostResponse response = postService.getPostBySlug(slug);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy chi tiết bài viết thành công", response));
    }

    // 7. API Sửa bài viết (Dành cho Tác giả)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request,
            Principal principal) {

        PostResponse response = postService.updatePost(id, request, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật bài viết thành công", response));
    }

    // 8. API Xóa bài viết (Dành cho Tác giả)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id, Principal principal) {
        postService.deletePost(id, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã xóa bài viết thành công", null));
    }

    // 9. API Ẩn bài viết (Dành cho Admin)
    @PatchMapping("/{id}/hide")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> hidePost(
            @PathVariable Long id,
            @RequestParam String reason,
            Principal principal) {
        postService.hidePost(id, principal.getName(), reason);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã ẩn bài viết thành công", null));
    }

    // 10. API Ghim bài viết Nổi bật (CHỨC NĂNG MỚI)
    @PutMapping("/{id}/featured")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> setFeatured(
            @PathVariable Long id,
            @RequestBody FeaturedRequest request) {

        PostResponse response = postService.setFeatured(id, request.isFeatured(), request.getPriority());

        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật trạng thái nổi bật thành công", response));
    }

    // ĐÃ XÓA HÀM mapToResponse VÌ KHÔNG CẦN THIẾT NỮA
}