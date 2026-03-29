package com.techblog.domain.comment.service;

import com.techblog.common.enums.CommentStatus;
import com.techblog.common.enums.ModerationAction;
import com.techblog.common.enums.ReportStatus;
import com.techblog.common.enums.TargetType;
import com.techblog.domain.comment.dto.*;
import com.techblog.domain.comment.model.Comment;
import com.techblog.domain.comment.model.CommentModerationLog;
import com.techblog.domain.comment.model.CommentReport;
import com.techblog.domain.comment.repository.CommentModerationLogRepository;
import com.techblog.domain.comment.repository.CommentReportRepository;
import com.techblog.domain.comment.repository.CommentRepository;
import com.techblog.domain.user.model.User;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentReportRepository reportRepository;
    private final CommentModerationLogRepository moderationLogRepository;

    @Transactional
    public CommentResponse addComment(User author, CreateCommentRequest request) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setContent(request.getContent());
        comment.setTargetType(request.getTargetType());
        comment.setTargetId(request.getTargetId());
        comment.setStatus(CommentStatus.VISIBLE); // Default to visible for now as per user request "everyone has right to comment"
        comment.setLikeCount(0);

        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bình luận cha"));
            comment.setParent(parent);
        }

        Comment savedComment = commentRepository.save(comment);
        return mapToResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsTree(TargetType targetType, Long targetId) {
        List<Comment> allComments = commentRepository.findByTargetTypeAndTargetIdAndStatusOrderByCreatedAtAsc(
                targetType, targetId, CommentStatus.VISIBLE);

        Map<Long, CommentResponse> responseMap = new LinkedHashMap<>();
        List<CommentResponse> roots = new ArrayList<>();

        // First pass: map all to responses
        for (Comment c : allComments) {
            CommentResponse resp = mapToResponse(c);
            responseMap.put(c.getId(), resp);
        }

        // Second pass: link parents and children
        for (Comment c : allComments) {
            CommentResponse current = responseMap.get(c.getId());
            if (c.getParent() != null && responseMap.containsKey(c.getParent().getId())) {
                CommentResponse parent = responseMap.get(c.getParent().getId());
                parent.getReplies().add(current);
            } else {
                roots.add(current);
            }
        }

        return roots;
    }

    @Transactional
    public void reportComment(User reporter, Long commentId, CommentReportRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bình luận"));

        CommentReport report = new CommentReport();
        report.setComment(comment);
        report.setReporter(reporter);
        report.setReason(request.getReason());
        report.setDetail(request.getDetail());
        report.setStatus(ReportStatus.PENDING);
        
        reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public List<CommentReportResponse> getAllReports() {
        return reportRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::mapToReportResponse).collect(Collectors.toList());
    }

    private CommentReportResponse mapToReportResponse(CommentReport report) {
        CommentReportResponse resp = new CommentReportResponse();
        resp.setId(report.getId());
        resp.setCommentId(report.getComment().getId());
        resp.setCommentContent(report.getComment().getContent());
        resp.setReporterId(report.getReporter().getId());
        resp.setReporterName(report.getReporter().getDisplayName());
        resp.setReason(report.getReason());
        resp.setDetail(report.getDetail());
        resp.setStatus(report.getStatus());
        resp.setCreatedAt(report.getCreatedAt());
        if (report.getResolvedBy() != null) {
            resp.setResolvedByName(report.getResolvedBy().getDisplayName());
        }
        resp.setResolvedAt(report.getResolvedAt());
        resp.setResolutionNote(report.getResolutionNote());
        return resp;
    }

    @Transactional
    public void resolveReport(User admin, Long reportId, ResolveReportRequest request) {
        CommentReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy báo cáo"));

        Comment comment = report.getComment();
        String action = request.getAction().toUpperCase();

        if ("HIDE".equals(action)) {
            moderateComment(admin, comment.getId(), CommentStatus.HIDDEN, "Xử lý báo cáo: " + request.getResolutionNote());
        } else if ("DELETE".equals(action)) {
            moderateComment(admin, comment.getId(), CommentStatus.DELETED, "Xử lý báo cáo: " + request.getResolutionNote());
        }

        report.setStatus(ReportStatus.RESOLVED);
        report.setResolvedBy(admin);
        report.setResolvedAt(LocalDateTime.now());
        report.setResolutionNote(request.getResolutionNote());
        reportRepository.save(report);
    }

    @Transactional
    public void moderateComment(User admin, Long commentId, CommentStatus toStatus, String reason) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bình luận"));

        CommentStatus fromStatus = comment.getStatus();
        comment.setStatus(toStatus);
        commentRepository.save(comment);

        CommentModerationLog log = new CommentModerationLog();
        log.setComment(comment);
        log.setModerator(admin);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setAction(toStatus == CommentStatus.VISIBLE ? ModerationAction.APPROVE : ModerationAction.HIDE);
        log.setReason(reason);
        log.setModeratedAt(LocalDateTime.now());
        moderationLogRepository.save(log);
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse resp = new CommentResponse();
        resp.setId(comment.getId());
        resp.setAuthorId(comment.getAuthor().getId());
        resp.setAuthorName(comment.getAuthor().getDisplayName());
        resp.setAuthorAvatar(comment.getAuthor().getAvatarUrl());
        resp.setContent(comment.getContent());
        resp.setTargetType(comment.getTargetType());
        resp.setTargetId(comment.getTargetId());
        resp.setParentId(comment.getParent() != null ? comment.getParent().getId() : null);
        resp.setStatus(comment.getStatus());
        resp.setLikeCount(comment.getLikeCount());
        resp.setCreatedAt(comment.getCreatedAt());
        return resp;
    }
}
