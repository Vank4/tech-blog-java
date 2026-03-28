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
import com.techblog.domain.post.dto.UpdatePostRequest;

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
    private final com.techblog.domain.tag.repository.TagRepository tagRepository;

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
                .tags(post.getTags())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }



    @Transactional
    public PostResponse createDraft(CreatePostRequest request, String email) { // Đổi kiểu trả về thành PostResponse
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

        // Gắn Tags
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            var tags = tagRepository.findAllById(request.getTagIds());
            post.setTags(new java.util.HashSet<>(tags));
        }

        Post savedPost = postRepository.save(post);

        // Trả về DTO ngay trong Transaction để tránh lỗi null Tags
        return mapToResponse(savedPost);
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


    @Transactional
    public PostResponse getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với đường dẫn này"));

        // Bảo mật: Người đọc bên ngoài chỉ được xem bài đã PUBLISHED
        if (post.getStatus() != ContentStatus.PUBLISHED) {
            throw new RuntimeException("Bài viết này chưa được xuất bản hoặc đã bị ẩn");
        }

        // Tăng lượt xem lên 1 đơn vị
        post.setViewCount(post.getViewCount() + 1);

        // Lưu lại số view mới vào Database
        postRepository.save(post);

        // Map sang DTO để giấu bớt các trường không cần thiết trước khi trả về
        return mapToResponse(post);
    }

    // 7. Tác giả cập nhật bài viết (UPDATE)
    @Transactional
    public PostResponse updatePost(Long postId, UpdatePostRequest request, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));

        // 1. Kiểm tra chính chủ: Lấy email người đang gửi request so sánh với email tác giả bài viết
        if (!post.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("Bạn không có quyền sửa bài viết của người khác");
        }

        // 2. MỞ KHÓA BẢO VỆ: Cho phép sửa bài Nháp (DRAFT), Bị từ chối (REJECTED), và Bị ẩn (HIDDEN)
        if (post.getStatus() != ContentStatus.DRAFT
                && post.getStatus() != ContentStatus.REJECTED
                && post.getStatus() != ContentStatus.HIDDEN) {
            throw new RuntimeException("Chỉ có thể sửa bài viết ở trạng thái Nháp, Bị từ chối hoặc Bị ẩn");
        }

        // 3. TỰ ĐỘNG HẠ CẤP: Nếu bài đang bị Ẩn hoặc Từ chối mà tác giả sửa, ép nó về lại DRAFT để chờ duyệt lại
        if (post.getStatus() == ContentStatus.HIDDEN || post.getStatus() == ContentStatus.REJECTED) {
            post.setStatus(ContentStatus.DRAFT);
        }

        // 4. Nếu đổi danh mục thì phải tìm danh mục mới
        if (!post.getCategory().getId().equals(request.getCategoryId())) {
            Category newCategory = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục mới"));
            post.setCategory(newCategory);
        }

        // 5. Nếu đổi Tiêu đề thì phải tạo lại Slug mới cho khớp
        if (!post.getTitle().equals(request.getTitle())) {
            post.setTitle(request.getTitle());
            post.setSlug(generateUniqueSlug(request.getTitle())); // Tái sử dụng hàm thuật toán Slug
        }

        // 6. Cập nhật các thông tin còn lại
        post.setContent(request.getContent());
        post.setSummary(request.getSummary());
        post.setThumbnailUrl(request.getThumbnailUrl());
        post.setAllowComments(request.isAllowComments());

        if (request.getTagIds() != null) {
            var tags = tagRepository.findAllById(request.getTagIds());
            post.setTags(new java.util.HashSet<>(tags));
        }

        // Lưu xuống DB và map ra Response trả về
        Post updatedPost = postRepository.save(post);
        return mapToResponse(updatedPost);
    }

    // 8. Tác giả XÓA bài viết (DELETE)
    @Transactional
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));

        // Kiểm tra chính chủ
        if (!post.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("Bạn không có quyền xóa bài viết của người khác");
        }

        // Chỉ cho phép xóa khi bài đang là NHÁP hoặc BỊ TỪ CHỐI
        if (post.getStatus() != ContentStatus.DRAFT && post.getStatus() != ContentStatus.REJECTED) {
            throw new RuntimeException("Chỉ có thể xóa bài viết ở trạng thái Nháp hoặc Bị từ chối");
        }

        // Xóa hẳn khỏi Database
        postRepository.delete(post);
    }

    // 9. Admin ẨN bài viết (HIDE) - Không xóa khỏi DB
    @Transactional
    public void hidePost(Long postId, String adminEmail, String reason) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
        User moderator = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Admin"));

        if (post.getStatus() != ContentStatus.PUBLISHED) {
            throw new RuntimeException("Chỉ có thể ẩn bài viết đang hiển thị (PUBLISHED)");
        }

        ContentStatus oldStatus = post.getStatus();
        post.setStatus(ContentStatus.HIDDEN);
        postRepository.save(post);

        // Lưu vết vào Log để sau này có bằng chứng
        saveModerationLog(post, moderator, oldStatus, ContentStatus.HIDDEN, ModerationAction.REJECT, reason);
    }

    // 10. Admin ghim bài viết nổi bật (FEATURED)
    @Transactional
    public PostResponse setFeatured(Long postId, boolean isFeatured, int priority) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết để ghim"));

        // Chỉ cho phép ghim những bài đã được xuất bản
        if (isFeatured && post.getStatus() != ContentStatus.PUBLISHED) {
            throw new RuntimeException("Chỉ có thể ghim bài viết đang ở trạng thái PUBLISHED");
        }

        post.setFeatured(isFeatured);
        post.setPriority(priority);

        Post updatedPost = postRepository.save(post);
        return mapToResponse(updatedPost);
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