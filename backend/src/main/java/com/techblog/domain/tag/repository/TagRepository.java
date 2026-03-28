package com.techblog.domain.tag.repository;

import com.techblog.domain.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    // Tìm tag theo tên (Dùng để check trùng lặp khi tạo mới)
    Optional<Tag> findByName(String name);

    // Tìm tag theo slug (Dùng cho URL sau này, ví dụ: /tags/java-core)
    Optional<Tag> findBySlug(String slug);
}