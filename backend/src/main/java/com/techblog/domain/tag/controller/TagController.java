package com.techblog.domain.tag.controller;

import com.techblog.domain.tag.model.Tag;
import com.techblog.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * Lấy danh sách toàn bộ Tag
     * GET http://localhost:8081/api/v1/tags
     */
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    /**
     * Tạo Tag mới
     * POST http://localhost:8081/api/v1/tags
     * Body (Text): Spring Boot
     */
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody String tagName) {
        // Lưu ý: Vì bạn đang gửi raw text từ Postman, tagName sẽ nhận trực tiếp chuỗi đó
        Tag createdTag = tagService.createTag(tagName.trim());
        return ResponseEntity.ok(createdTag);
    }
}