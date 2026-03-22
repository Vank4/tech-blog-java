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

    @PostMapping
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')") // Khóa bảo mật: Phải có quyền mới được vào
    public ResponseEntity<ApiResponse<PostResponse>> createDraft(
            @Valid @RequestBody CreatePostRequest request,
            Principal principal) { // Spring sẽ tự động nhét thông tin Token vào biến này

        // Lấy email (hoặc username) của người dùng từ Token
        String email = principal.getName();

        // Truyền email thật xuống Service xử lý
        Post post = postService.createDraft(request, email);

        PostResponse response = mapToResponse(post);

        return ResponseEntity.ok(new ApiResponse<>(true, "Tạo bản nháp thành công", response));
    }

    // --- HÀM PHỤ TRỢ (Giữ nguyên như cũ) ---
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

                // Lấy ID và Tên danh mục
                .categoryId(post.getCategory() != null ? post.getCategory().getId() : null)
                .categoryName(post.getCategory() != null ? post.getCategory().getName() : null)

                // Lấy ID và Tên hiển thị của tác giả
                .authorId(post.getAuthor() != null ? post.getAuthor().getId() : null)
                .authorName(post.getAuthor() != null ? post.getAuthor().getDisplayName() : null)

                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}