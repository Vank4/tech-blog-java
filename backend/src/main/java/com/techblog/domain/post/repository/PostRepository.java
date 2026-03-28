package com.techblog.domain.post.repository;

import com.techblog.common.enums.ContentStatus;
import com.techblog.domain.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findBySlug(String slug);

    boolean existsBySlug(String slug);

    // Tìm theo trạng thái và có phân trang
    Page<Post> findByStatus(ContentStatus status, Pageable pageable);

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE p.status = :status " +
            "AND (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.summary) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:tagId IS NULL OR t.id = :tagId)")
    Page<Post> searchPosts(@Param("keyword") String keyword,
                           @Param("tagId") Long tagId,
                           @Param("status") ContentStatus status,
                           Pageable pageable);
}