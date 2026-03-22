package com.techblog.domain.post.service;

import com.techblog.common.enums.ContentStatus;
import com.techblog.domain.category.model.Category;
import com.techblog.domain.category.repository.CategoryRepository;
import com.techblog.domain.post.dto.CreatePostRequest;
import com.techblog.domain.post.model.Post;
import com.techblog.domain.post.repository.PostRepository;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post createDraft(CreatePostRequest request, String email) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        String slug = generateUniqueSlug(request.getTitle());

        Post post = new Post();
        // --- CÁC DÒNG QUAN TRỌNG CẦN KIỂM TRA ---
        post.setTitle(request.getTitle());
        post.setContent(request.getContent()); // <-- THIẾU DÒNG NÀY SẼ GÂY LỖI 500
        post.setSummary(request.getSummary());
        post.setThumbnailUrl(request.getThumbnailUrl());
        // ---------------------------------------

        post.setCategory(category);
        post.setAuthor(author);
        post.setSlug(slug);
        post.setStatus(ContentStatus.DRAFT);
        post.setAllowComments(request.isAllowComments());

        return postRepository.save(post);
    }

    // ================== CÁC HÀM PHỤ TRỢ (MAGIC Ở ĐÂY) ==================

    // Hàm 1: Đảm bảo đường dẫn (Slug) là ĐỘC NHẤT
    private String generateUniqueSlug(String title) {
        String baseSlug = toSlug(title); // Biến "Học Java" thành "hoc-java"
        String uniqueSlug = baseSlug;
        int count = 1;

        // Dùng hàm bạn vừa thêm ở Repository để check trùng lặp!
        // Nếu "hoc-java" đã tồn tại, nó sẽ tự đổi thành "hoc-java-1", "hoc-java-2"...
        while (postRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + count;
            count++;
        }
        return uniqueSlug;
    }

    // Hàm 2: Xóa dấu tiếng Việt và ký tự đặc biệt
    private String toSlug(String input) {
        if (input == null || input.trim().isEmpty()) return "";
        // Chuyển tiếng Việt có dấu thành không dấu
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noDiacritics = pattern.matcher(normalized).replaceAll("");

        return noDiacritics.toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9\\s-]", "") // Xóa mọi thứ không phải chữ cái, số, khoảng trắng hoặc gạch ngang
                .replaceAll("\\s+", "-")         // Biến khoảng trắng thành gạch ngang
                .replaceAll("-+", "-");          // Gộp nhiều gạch ngang liên tiếp thành 1 cái
    }
}