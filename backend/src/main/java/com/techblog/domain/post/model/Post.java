package com.techblog.domain.post.model;

import com.techblog.common.audit.BaseAuditEntity;
import com.techblog.common.enums.PostStatus;
import com.techblog.domain.category.model.Category;
import com.techblog.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "posts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_posts_slug", columnNames = "slug")
        }
)
public class Post extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "thumbnail_url", length = 255)
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostStatus status = PostStatus.DRAFT;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;
}