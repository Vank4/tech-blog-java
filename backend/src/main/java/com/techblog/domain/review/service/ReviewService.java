package com.techblog.domain.review.service;

import com.techblog.common.enums.ContentStatus;
import com.techblog.common.enums.ModerationAction;
import com.techblog.domain.product.model.Product;
import com.techblog.domain.product.repository.ProductRepository;
import com.techblog.domain.review.dto.CreateReviewRequest;
import com.techblog.domain.review.dto.ReviewResponse;
import com.techblog.domain.review.dto.UpdateReviewRequest;
import com.techblog.domain.review.model.Review;
import com.techblog.domain.review.model.ReviewModerationLog;
import com.techblog.domain.review.model.ReviewScore;
import com.techblog.domain.product.model.ProductImage;
import com.techblog.domain.product.repository.ProductImageRepository;
import com.techblog.domain.review.repository.ReviewModerationLogRepository;
import com.techblog.domain.review.repository.ReviewRepository;
import com.techblog.domain.review.repository.ReviewScoreRepository;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewScoreRepository scoreRepository;
    private final ReviewModerationLogRepository moderationLogRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;

    // 1. Tạo bản nháp của Review
    @Transactional
    public ReviewResponse createDraft(CreateReviewRequest request, String email) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + email));

        Review review = new Review();
        review.setProduct(product);
        review.setAuthor(author);
        review.setTitle(request.getTitle());
        review.setSlug(generateUniqueSlug(request.getTitle()));
        review.setSummary(request.getSummary());
        review.setContent(request.getContent());
        review.setPros(request.getPros());
        review.setCons(request.getCons());
        review.setOverallScore(request.getOverallScore());
        review.setStatus(ContentStatus.DRAFT);
        review.setAllowComments(request.isAllowComments());

        Review savedReview = reviewRepository.save(review);

        // Lưu điểm số chi tiết
        if (request.getScores() != null) {
            List<ReviewScore> scores = request.getScores().stream().map(s -> {
                ReviewScore score = new ReviewScore();
                score.setReview(savedReview);
                score.setCriterion(s.getCriterion());
                score.setScore(s.getScore());
                score.setMaxScore(s.getMaxScore());
                score.setNote(s.getNote());
                score.setDisplayOrder(s.getDisplayOrder());
                return score;
            }).collect(Collectors.toList());
            scoreRepository.saveAll(scores);
        }

        return mapToResponse(savedReview);
    }

    // 2. Gửi duyệt (DRAFT/REJECTED -> PENDING)
    @Transactional
    public void submitReview(Long reviewId, String email) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy review"));
        if (!review.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("Bạn không có quyền gửi bài viết này");
        }
        if (review.getStatus() != ContentStatus.DRAFT && review.getStatus() != ContentStatus.REJECTED) {
            throw new RuntimeException("Chỉ bài viết bản nháp hoặc bị từ chối mới có thể gửi duyệt");
        }

        review.setStatus(ContentStatus.PENDING);
        review.setSubmittedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    // 3. Cập nhật Review
    @Transactional
    public ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request, String email) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy review"));
        if (!review.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("Bạn không có quyền sửa bài viết này");
        }

        if (review.getStatus() == ContentStatus.PUBLISHED) {
            throw new RuntimeException("Bài viết đã đăng không thể sửa trực tiếp. Hãy chuyển về nháp.");
        }

        if (review.getStatus() == ContentStatus.REJECTED) {
            review.setStatus(ContentStatus.DRAFT);
        }

        if (!review.getTitle().equals(request.getTitle())) {
            review.setTitle(request.getTitle());
            review.setSlug(generateUniqueSlug(request.getTitle()));
        }
        review.setSummary(request.getSummary());
        review.setContent(request.getContent());
        review.setPros(request.getPros());
        review.setCons(request.getCons());
        review.setOverallScore(request.getOverallScore());

        if (request.getAllowComments() != null) {
            review.setAllowComments(request.getAllowComments());
        }

        // Cập nhật điểm số (Xóa cũ thêm mới cho đơn giản)
        List<ReviewScore> oldScores = scoreRepository.findByReviewIdOrderByDisplayOrderAsc(reviewId);
        scoreRepository.deleteAll(oldScores);

        if (request.getScores() != null) {
            List<ReviewScore> newScores = request.getScores().stream().map(s -> {
                ReviewScore score = new ReviewScore();
                score.setReview(review);
                score.setCriterion(s.getCriterion());
                score.setScore(s.getScore());
                score.setMaxScore(s.getMaxScore());
                score.setNote(s.getNote());
                score.setDisplayOrder(s.getDisplayOrder());
                return score;
            }).collect(Collectors.toList());
            scoreRepository.saveAll(newScores);
        }

        return mapToResponse(reviewRepository.save(review));
    }

    // 4. Admin: Phê duyệt
    @Transactional
    public void approveReview(Long reviewId, String adminEmail) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User moderator = userRepository.findByEmail(adminEmail).orElseThrow();
        ContentStatus oldStatus = review.getStatus();

        review.setStatus(ContentStatus.PUBLISHED);
        review.setPublishedAt(LocalDateTime.now());
        reviewRepository.save(review);

        saveModerationLog(review, moderator, oldStatus, ContentStatus.PUBLISHED, ModerationAction.APPROVE, "Phê duyệt nội dung");
    }

    // 5. Admin: Từ chối
    @Transactional
    public void rejectReview(Long reviewId, String adminEmail, String reason) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User moderator = userRepository.findByEmail(adminEmail).orElseThrow();
        ContentStatus oldStatus = review.getStatus();

        review.setStatus(ContentStatus.REJECTED);
        reviewRepository.save(review);

        saveModerationLog(review, moderator, oldStatus, ContentStatus.REJECTED, ModerationAction.REJECT, reason);
    }

    // 6. Admin: Ẩn review
    @Transactional
    public void hideReview(Long reviewId, String adminEmail, String reason) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User moderator = userRepository.findByEmail(adminEmail).orElseThrow();
        ContentStatus oldStatus = review.getStatus();

        review.setStatus(ContentStatus.HIDDEN);
        reviewRepository.save(review);

        saveModerationLog(review, moderator, oldStatus, ContentStatus.HIDDEN, ModerationAction.HIDE, reason);
    }

    // -------------------------------------------------------------------------
    // Query Methods
    // -------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<ReviewResponse> getLatestPublishedReviews(int limit) {
        return reviewRepository.findAllByStatusOrderByPublishedAtDesc(ContentStatus.PUBLISHED)
                .stream()
                .limit(limit)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getPendingReviews() {
        return reviewRepository.findAllByStatusOrderByCreatedAtDesc(ContentStatus.PENDING)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getAuthorReviews(String email) {
        return reviewRepository.findAllByAuthorEmailOrderByCreatedAtDesc(email)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllReviewsForAdmin() {
        return reviewRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReviewBySlug(String slug) {
        return reviewRepository.findBySlug(slug)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy review với slug: " + slug));
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long id) {
        return reviewRepository.findById(id).map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy review"));
    }

    // -------------------------------------------------------------------------
    // Private Helpers
    // -------------------------------------------------------------------------

    private ReviewResponse mapToResponse(Review review) {
        List<ReviewScore> scores = scoreRepository.findByReviewIdOrderByDisplayOrderAsc(review.getId());
        
        List<ReviewResponse.ReviewScoreDTO> scoreDTOs = scores.stream()
                .map(s -> ReviewResponse.ReviewScoreDTO.builder()
                        .criterion(s.getCriterion())
                        .score(s.getScore())
                        .maxScore(s.getMaxScore())
                        .note(s.getNote())
                        .displayOrder(s.getDisplayOrder())
                        .build())
                .collect(Collectors.toList());

        return ReviewResponse.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .productName(review.getProduct().getName())
                .productSlug(review.getProduct().getSlug())
                .authorId(review.getAuthor().getId())
                .authorName(review.getAuthor().getDisplayName())
                .title(review.getTitle())
                .slug(review.getSlug())
                .summary(review.getSummary())
                .content(review.getContent())
                .pros(review.getPros())
                .cons(review.getCons())
                .overallScore(review.getOverallScore())
                .status(review.getStatus())
                .submittedAt(review.getSubmittedAt())
                .publishedAt(review.getPublishedAt())
                .scores(scoreDTOs)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .allowComments(review.isAllowComments())
                .productImageUrl(productImageRepository.findByProductIdAndPrimaryTrue(review.getProduct().getId())
                        .map(ProductImage::getImageUrl).orElse(null))
                .build();
    }

    private void saveModerationLog(Review review, User moderator, ContentStatus from, ContentStatus to, ModerationAction action, String reason) {
        ReviewModerationLog log = new ReviewModerationLog();
        log.setReview(review);
        log.setModerator(moderator);
        log.setFromStatus(from);
        log.setToStatus(to);
        log.setAction(action);
        log.setReason(reason);
        log.setModeratedAt(LocalDateTime.now());
        moderationLogRepository.save(log);
    }

    private String generateUniqueSlug(String title) {
        String baseSlug = toSlug(title);
        String uniqueSlug = baseSlug;
        int count = 1;
        while (reviewRepository.findBySlug(uniqueSlug).isPresent()) {
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
                .replaceAll("-+", "-")
                .trim();
    }
}
