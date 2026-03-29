package com.techblog.domain.post.service;

import com.techblog.common.enums.ContentStatus;
import com.techblog.common.enums.ModerationAction;
import com.techblog.domain.category.model.Category;
import com.techblog.domain.category.repository.CategoryRepository;
import com.techblog.domain.post.dto.CreatePostRequest;
import com.techblog.domain.post.dto.ModerationLogResponse;
import com.techblog.domain.post.dto.PostResponse;
import com.techblog.domain.post.dto.UpdatePostRequest;
import com.techblog.domain.post.model.Post;
import com.techblog.domain.post.model.PostModerationLog;
import com.techblog.domain.post.repository.PostModerationLogRepository;
import com.techblog.domain.post.repository.PostRepository;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PostModerationLogRepository moderationLogRepository;
    private final com.techblog.domain.tag.repository.TagRepository tagRepository;

    // 1. Lấy danh sách bài viết
    @Transactional(readOnly = true)
    public Page<PostResponse> getPosts(ContentStatus status, String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> postPage;

        if (title != null && !title.trim().isEmpty()) {
            if (status != null) {
                postPage = postRepository.findByStatusAndTitleContainingIgnoreCase(status, title, pageable);
            } else {
                postPage = postRepository.findByTitleContainingIgnoreCase(title, pageable);
            }
        } else if (status != null) {
            postPage = postRepository.findByStatus(status, pageable);
        } else {
            postPage = postRepository.findAll(pageable);
        }

        return postPage.map(this::mapToResponse);
    }

    // 2. Mapper: Chuyển Entity sang DTO (Đã tích hợp lấy lý do REJECT)
    private PostResponse mapToResponse(Post post) {
        var tagList = post.getTags() != null
                ? new java.util.HashSet<>(post.getTags())
                : new java.util.HashSet<com.techblog.domain.tag.model.Tag>();

        // Tạo DTO cơ bản
        PostResponse response = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .summary(post.getSummary())
                .content(post.getContent())
                .thumbnailUrl(post.getThumbnailUrl())
                .status(post.getStatus())
                .viewCount(post.getViewCount())
                .allowComments(post.isAllowComments())
                .categoryId(post.getCategory() != null ? post.getCategory().getId() : null)
                .categoryName(post.getCategory() != null ? post.getCategory().getName() : null)
                .authorId(post.getAuthor() != null ? post.getAuthor().getId() : null)
                .authorName(post.getAuthor() != null ? post.getAuthor().getDisplayName() : null)
                .tags(tagList)
                .isFeatured(post.isFeatured())
                .priority(post.getPriority())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

        // MỚI: Nếu bài bị REJECTED, lấy lý do từ log mới nhất
        if (post.getStatus() == ContentStatus.REJECTED) {
            moderationLogRepository.findTopByPostOrderByModeratedAtDesc(post)
                    .ifPresent(log -> response.setRejectionReason(log.getReason()));
        }

        return response;
    }

    // 3. Tạo bài viết nháp
    @Transactional
    public PostResponse createDraft(CreatePostRequest request, String email) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + email));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSummary(request.getSummary());
        post.setThumbnailUrl(request.getThumbnailUrl());
        post.setCategory(category);
        post.setAuthor(author);
        post.setSlug(generateUniqueSlug(request.getTitle()));
        post.setStatus(ContentStatus.DRAFT);
        post.setAllowComments(request.isAllowComments());

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            var tags = tagRepository.findAllById(request.getTagIds());
            post.setTags(new java.util.HashSet<>(tags));
        }

        return mapToResponse(postRepository.save(post));
    }

    // 4. Gửi duyệt
    @Transactional
    public void submitPost(Long postId, String email) {
        Post post = postRepository.findById(postId).orElseThrow();
        if (!post.getAuthor().getEmail().equals(email)) throw new RuntimeException("Không có quyền");
        post.setStatus(ContentStatus.PENDING);
        post.setSubmittedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    // 5. Duyệt bài
    @Transactional
    public void approvePost(Long postId, String adminEmail) {
        Post post = postRepository.findById(postId).orElseThrow();
        User moderator = userRepository.findByEmail(adminEmail).orElseThrow();
        ContentStatus oldStatus = post.getStatus();
        post.setStatus(ContentStatus.PUBLISHED);
        post.setPublishedAt(LocalDateTime.now());
        postRepository.save(post);
        saveModerationLog(post, moderator, oldStatus, ContentStatus.PUBLISHED, ModerationAction.APPROVE, "Phê duyệt");
    }

    // 6. Từ chối bài
    @Transactional
    public void rejectPost(Long postId, String adminEmail, String reason) {
        Post post = postRepository.findById(postId).orElseThrow();
        User moderator = userRepository.findByEmail(adminEmail).orElseThrow();
        ContentStatus oldStatus = post.getStatus();
        post.setStatus(ContentStatus.REJECTED);
        postRepository.save(post);
        saveModerationLog(post, moderator, oldStatus, ContentStatus.REJECTED, ModerationAction.REJECT, reason);
    }

    // 7. Ẩn bài
    @Transactional
    public void hidePost(Long postId, String adminEmail, String reason) {
        Post post = postRepository.findById(postId).orElseThrow();
        User moderator = userRepository.findByEmail(adminEmail).orElseThrow();
        ContentStatus oldStatus = post.getStatus();
        post.setStatus(ContentStatus.HIDDEN);
        postRepository.save(post);
        saveModerationLog(post, moderator, oldStatus, ContentStatus.HIDDEN, ModerationAction.REJECT, reason);
    }

    // 8. Lấy bài theo Slug
    @Transactional
    public PostResponse getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug).orElseThrow();
        if (post.getStatus() != ContentStatus.PUBLISHED) throw new RuntimeException("Bài chưa đăng");
        post.setViewCount(post.getViewCount() + 1);
        return mapToResponse(postRepository.save(post));
    }

    // 9. Cập nhật bài
    @Transactional
    public PostResponse updatePost(Long postId, UpdatePostRequest request, String email) {
        Post post = postRepository.findById(postId).orElseThrow();
        if (!post.getAuthor().getEmail().equals(email)) throw new RuntimeException("Không có quyền");

        if (post.getStatus() == ContentStatus.HIDDEN || post.getStatus() == ContentStatus.REJECTED) {
            post.setStatus(ContentStatus.DRAFT);
        }

        if (!post.getTitle().equals(request.getTitle())) {
            post.setTitle(request.getTitle());
            post.setSlug(generateUniqueSlug(request.getTitle()));
        }
        post.setContent(request.getContent());
        post.setSummary(request.getSummary());
        post.setThumbnailUrl(request.getThumbnailUrl());
        post.setAllowComments(request.isAllowComments());

        if (request.getTagIds() != null) {
            var tags = tagRepository.findAllById(request.getTagIds());
            post.setTags(new java.util.HashSet<>(tags));
        }

        return mapToResponse(postRepository.save(post));
    }

    // 10. Xóa bài
    @Transactional
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId).orElseThrow();
        if (!post.getAuthor().getEmail().equals(email)) throw new RuntimeException("Không có quyền");
        postRepository.delete(post);
    }

    // 11. Ghim bài
    @Transactional
    public PostResponse setFeatured(Long postId, Boolean isFeatured, Integer priority) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setFeatured(isFeatured != null ? isFeatured : false);
        post.setPriority(priority != null ? priority : 0);
        return mapToResponse(postRepository.save(post));
    }

    // 12. Lấy bài theo ID
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        return mapToResponse(post);
    }

    // 13. Search Public
    @Transactional(readOnly = true)
    public Page<PostResponse> searchPublicPosts(String keyword, Long tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("isFeatured").descending().and(Sort.by("createdAt").descending()));
        return postRepository.searchPosts(keyword, tagId, ContentStatus.PUBLISHED, pageable).map(this::mapToResponse);
    }

    // 14. Lấy Logs
    @Transactional(readOnly = true)
    public List<ModerationLogResponse> getAllModerationLogs() {
        return moderationLogRepository.findAllByOrderByModeratedAtDesc().stream()
                .map(log -> ModerationLogResponse.builder()
                        .id(log.getId())
                        .postTitle(log.getPost().getTitle())
                        .action(log.getAction().name())
                        .adminName(log.getModerator().getDisplayName())
                        .reason(log.getReason())
                        .createdAt(log.getModeratedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private void saveModerationLog(Post post, User moderator, ContentStatus from, ContentStatus to, ModerationAction action, String reason) {
        PostModerationLog log = new PostModerationLog();
        log.setPost(post);
        log.setModerator(moderator);
        log.setFromStatus(from);
        log.setToStatus(to);
        log.setAction(action);
        log.setReason(reason);
        moderationLogRepository.save(log);
    }

    private String generateUniqueSlug(String title) {
        String baseSlug = toSlug(title);
        String uniqueSlug = baseSlug;
        int count = 1;
        while (postRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + count;
            count++;
        }
        return uniqueSlug;
    }

    private String toSlug(String input) {
        if (input == null || input.trim().isEmpty()) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noDiacritics = pattern.matcher(normalized).replaceAll("");
        return noDiacritics.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-").replaceAll("-+", "-");
    }
}