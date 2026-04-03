package com.techblog.domain.post.model;

import com.techblog.common.audit.Auditable;
import com.techblog.common.enums.ContentStatus;
import com.techblog.domain.category.model.Category;
import com.techblog.domain.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import com.techblog.domain.tag.model.Tag;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "posts", uniqueConstraints = {
        @UniqueConstraint(name = "uk_posts_slug", columnNames = "slug")
})
public class Post extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "slug", nullable = false, length = 255)
    private String slug;

    @Column(name = "summary", length = 1000)
    private String summary;

    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ContentStatus status = ContentStatus.DRAFT;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "view_count", nullable = false)
    private long viewCount;

    @Column(name = "allow_comments", nullable = false)
    private boolean allowComments = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL, // "Chìa khóa" ở đây: Hành động xóa sẽ lan truyền sang các Log
            orphanRemoval = true       // Nếu Log không còn thuộc về Post nào thì cũng xóa luôn
    )
    private List<PostModerationLog> moderationLogs = new ArrayList<>();

    @Column(name = "is_featured", nullable = false)
    private boolean isFeatured = false;

    @Column(name = "priority", nullable = false)
    private int priority = 0;
}
