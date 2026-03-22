package com.techblog.domain.post.service;

import com.techblog.common.enums.ContentStatus;
import com.techblog.common.enums.ModerationAction; // Import Enum quan trọng này
import com.techblog.domain.category.model.Category;
import com.techblog.domain.category.repository.CategoryRepository;
import com.techblog.domain.post.dto.CreatePostRequest;
import com.techblog.domain.post.model.Post;
import com.techblog.domain.post.model.PostModerationLog;
import com.techblog.domain.post.repository.PostModerationLogRepository;
import com.techblog.domain.post.repository.PostRepository;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    // 1. Tác giả tạo bản nháp (DRAFT)
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

    // 2. Tác giả gửi bài duyệt (DRAFT -> PENDING)
    @Transactional
    public void submitPost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));

        if (!post.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("Bạn không có quyền gửi bài viết của người khác");
        }

        if (post.getStatus() != ContentStatus.DRAFT && post.getStatus() != ContentStatus.REJECTED) {
            throw new RuntimeException("Trạng thái bài viết không hợp lệ để gửi duyệt");
        }

        post.setStatus(ContentStatus.PENDING);
        post.setSubmittedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    // 3. Admin Phê duyệt bài viết (PENDING -> PUBLISHED)
    @Transactional
    public void approvePost(Long postId, String adminEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
        User moderator = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Admin"));

        if (post.getStatus() != ContentStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể duyệt bài đang ở trạng thái Chờ duyệt");
        }

        ContentStatus oldStatus = post.getStatus();
        post.setStatus(ContentStatus.PUBLISHED);
        post.setPublishedAt(LocalDateTime.now());
        postRepository.save(post);

        // Đã sửa thành ModerationAction.APPROVE (kiểu Enum)
        saveModerationLog(post, moderator, oldStatus, ContentStatus.PUBLISHED, ModerationAction.APPROVE, "Bài viết hợp lệ");
    }

    // 4. Admin Từ chối bài viết (PENDING -> REJECTED)
    @Transactional
    public void rejectPost(Long postId, String adminEmail, String reason) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
        User moderator = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Admin"));

        ContentStatus oldStatus = post.getStatus();
        post.setStatus(ContentStatus.REJECTED);
        postRepository.save(post);

        // Đã sửa thành ModerationAction.REJECT (kiểu Enum)
        saveModerationLog(post, moderator, oldStatus, ContentStatus.REJECTED, ModerationAction.REJECT, reason);
    }

    // Hàm lưu Log trung tâm - Tham số action đã đổi sang ModerationAction
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

    // --- CÁC HÀM UTILS (SLUG) ---
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

        return noDiacritics.toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}