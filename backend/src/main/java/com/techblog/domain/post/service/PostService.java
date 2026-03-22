package com.techblog.domain.post.service;

import com.techblog.common.enums.ContentStatus;
import com.techblog.common.enums.ModerationAction;
import com.techblog.domain.category.model.Category;
import com.techblog.domain.category.repository.CategoryRepository;
import com.techblog.domain.post.dto.CreatePostRequest;
import com.techblog.domain.post.dto.PostResponse; // Import thêm cái này
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
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PostModerationLogRepository moderationLogRepository;

    // 1. Lấy danh sách bài viết (Đã fix lỗi LazyInitialization)
    @Transactional(readOnly = true) // Giữ session mở để lấy được Category/Author name
    public Page<PostResponse> getPosts(ContentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Post> postPage;
        if (status != null) {
            postPage = postRepository.findByStatus(status, pageable);
        } else {
            postPage = postRepository.findAll(pageable);
        }

        // Map sang Response ngay tại đây khi Transaction vẫn đang mở
        return postPage.map(this::mapToResponse);
    }

    // 2. Chuyển đổi Entity sang DTO (Mapper nội bộ)
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



    @Transactional
    public Post createDraft(CreatePostRequest request, String email) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        String slug = generateUniqueSlug(request.getTitle());
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSummary(request.getSummary());
        post.setThumbnailUrl(request.getThumbnailUrl());
        post.setCategory(category);
        post.setAuthor(author);
        post.setSlug(slug);
        post.setStatus(ContentStatus.DRAFT);
        post.setAllowComments(request.isAllowComments());
        return postRepository.save(post);
    }

    @Transactional
    public void submitPost(Long postId, String email) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
        if (!post.getAuthor().getEmail().equals(email)) throw new RuntimeException("Không có quyền");
        post.setStatus(ContentStatus.PENDING);
        post.setSubmittedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    @Transactional
    public void approvePost(Long postId, String adminEmail) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
        User moderator = userRepository.findByEmail(adminEmail).orElseThrow(() -> new RuntimeException("Không tìm Admin"));
        ContentStatus oldStatus = post.getStatus();
        post.setStatus(ContentStatus.PUBLISHED);
        post.setPublishedAt(LocalDateTime.now());
        postRepository.save(post);
        saveModerationLog(post, moderator, oldStatus, ContentStatus.PUBLISHED, ModerationAction.APPROVE, "Hợp lệ");
    }

    @Transactional
    public void rejectPost(Long postId, String adminEmail, String reason) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
        User moderator = userRepository.findByEmail(adminEmail).orElseThrow(() -> new RuntimeException("Không tìm Admin"));
        ContentStatus oldStatus = post.getStatus();
        post.setStatus(ContentStatus.REJECTED);
        postRepository.save(post);
        saveModerationLog(post, moderator, oldStatus, ContentStatus.REJECTED, ModerationAction.REJECT, reason);
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