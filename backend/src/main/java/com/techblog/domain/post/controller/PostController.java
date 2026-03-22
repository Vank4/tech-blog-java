package com.techblog.domain.post.controller;

import com.techblog.common.response.ApiResponse;
import com.techblog.domain.post.dto.CreatePostRequest;
import com.techblog.domain.post.dto.PostResponse;
import com.techblog.domain.post.model.Post;
import com.techblog.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 1. API Tạo bản nháp (DRAFT)
    @PostMapping
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> createDraft(
            @Valid @RequestBody CreatePostRequest request,
            Principal principal) {
        String email = principal.getName();
        Post post = postService.createDraft(request, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tạo bản nháp thành công", mapToResponse(post)));
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
    @PreAuthorize("hasRole('ADMIN')") // Chỉ ADMIN mới có quyền này
    public ResponseEntity<ApiResponse<Void>> approvePost(@PathVariable Long id, Principal principal) {
        postService.approvePost(id, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã phê duyệt và xuất bản bài viết", null));
    }

    // 4. API Admin từ chối (PENDING -> REJECTED)
    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')") // Chỉ ADMIN mới có quyền này
    public ResponseEntity<ApiResponse<Void>> rejectPost(
            @PathVariable Long id,
            @RequestParam String reason,
            Principal principal) {
        postService.rejectPost(id, principal.getName(), reason);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đã từ chối bài viết", null));
    }

    // --- HÀM PHỤ TRỢ (Đã nạp tên đầy đủ) ---
    private PostResponse mapToResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .summary(post.getSummary())
                .thumbnailUrl(post.getThumbnailUrl())
                .status(post.getStatus())
                .viewCount(post.getViewCount())
                .allowComments(post.isAllowComments())
                .categoryId(post.getCategory() != null ? post.getCategory().getId() : null)
                .categoryName(post.getCategory() != null ? post.getCategory().getName() : null)
                .authorId(post.getAuthor() != null ? post.getAuthor().getId() : null)
                .authorName(post.getAuthor() != null ? post.getAuthor().getDisplayName() : null)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}