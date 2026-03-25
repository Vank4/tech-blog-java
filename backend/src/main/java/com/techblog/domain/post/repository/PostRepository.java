package com.techblog.domain.post.repository;

import com.techblog.common.enums.ContentStatus;
import com.techblog.domain.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findBySlug(String slug);

    boolean existsBySlug(String slug);

    // Tìm theo trạng thái và có phân trang
    Page<Post> findByStatus(ContentStatus status, Pageable pageable);

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);
}