package com.techblog.domain.tag.service;

import com.techblog.domain.tag.model.Tag;
import com.techblog.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.text.Normalizer;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Transactional
    public Tag createTag(String name) {
        // Kiểm tra trùng lặp
        tagRepository.findByName(name).ifPresent(t -> {
            throw new RuntimeException("Tag '" + name + "' đã tồn tại rồi nhé Phong!");
        });

        Tag tag = new Tag();
        tag.setName(name);
        tag.setSlug(generateSlug(name));

        return tagRepository.save(tag);
    }

    // Hàm hỗ trợ tạo slug tiếng Việt chuẩn (Ví dụ: "Lập Trình" -> "lap-trinh")
    private String generateSlug(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String slug = pattern.matcher(nfdNormalizedString).replaceAll("");
        return slug.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("^-+|-+$", "");
    }

    @Transactional
    public Tag updateTag(Long id, String newName) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thẻ (Tag)"));

        // Kiểm tra trùng tên (nếu tên mới khác tên cũ)
        if (!tag.getName().equalsIgnoreCase(newName.trim())) {
            tagRepository.findByName(newName.trim()).ifPresent(t -> {
                throw new RuntimeException("Tag '" + newName + "' đã tồn tại!");
            });
            tag.setName(newName.trim());
            tag.setSlug(generateSlug(newName.trim())); // Tạo lại slug mới
        }
        return tagRepository.save(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thẻ (Tag)"));
        // TODO: Nếu sau này cần kiểm tra Tag đang có bài viết không thì thêm logic vào đây
        tagRepository.delete(tag);
    }
}