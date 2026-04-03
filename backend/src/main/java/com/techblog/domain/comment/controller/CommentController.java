package com.techblog.domain.comment.controller;

import com.techblog.common.response.ApiResponse;
import com.techblog.common.enums.TargetType;
import com.techblog.domain.comment.dto.CommentReportRequest;
import com.techblog.domain.comment.dto.CommentResponse;
import com.techblog.domain.comment.dto.CreateCommentRequest;
import com.techblog.domain.comment.service.CommentService;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    @GetMapping("/{targetType}/{targetId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable TargetType targetType,
            @PathVariable Long targetId) {
        List<CommentResponse> tree = commentService.getCommentsTree(targetType, targetId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách bình luận thành công", tree));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @Valid @RequestBody CreateCommentRequest request,
            Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        CommentResponse response = commentService.addComment(user, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Đăng bình luận thành công", response));
    }

    @PostMapping("/{id}/report")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> reportComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentReportRequest request,
            Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        commentService.reportComment(user, id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Báo cáo vi phạm thành công", null));
    }
}
