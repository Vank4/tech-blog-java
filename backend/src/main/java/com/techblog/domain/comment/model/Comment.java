package com.techblog.domain.comment.model;

import com.techblog.common.audit.BaseAuditEntity;
import com.techblog.common.enums.CommentStatus;
import com.techblog.common.enums.CommentTargetType;
import com.techblog.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(name = "idx_comments_target", columnList = "target_type,target_id"),
                @Index(name = "idx_comments_parent", columnList = "parent_id")
        }
)
public class Comment extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private CommentTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CommentStatus status = CommentStatus.VISIBLE;
}