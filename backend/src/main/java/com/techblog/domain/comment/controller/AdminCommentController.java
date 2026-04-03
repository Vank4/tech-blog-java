package com.techblog.domain.comment.controller;

import com.techblog.common.enums.CommentStatus;
import com.techblog.common.response.ApiResponse;
import com.techblog.domain.comment.dto.CommentReportResponse;
import com.techblog.domain.comment.dto.ResolveReportRequest;
import com.techblog.domain.comment.service.CommentService;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/comments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<List<CommentReportResponse>>> getAllReports() {
        List<CommentReportResponse> reports = commentService.getAllReports();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy toàn bộ danh sách báo cáo thành công", reports));
    }

    @PatchMapping("/reports/{id}/resolve")
    public ResponseEntity<ApiResponse<Void>> resolveReport(
            @PathVariable Long id,
            @RequestBody ResolveReportRequest request,
            Principal principal) {
        User admin = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        commentService.resolveReport(admin, id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Xử lý báo cáo thành công", null));
    }

    @PatchMapping("/{id}/moderate")
    public ResponseEntity<ApiResponse<Void>> moderateComment(
            @PathVariable Long id,
            @RequestParam CommentStatus status,
            @RequestParam String reason,
            Principal principal) {
        User admin = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        commentService.moderateComment(admin, id, status, reason);
        return ResponseEntity.ok(new ApiResponse<>(true, "Kiểm duyệt bình luận thành công", null));
    }
}
