package com.techblog.domain.post.repository;

import com.techblog.common.enums.ContentStatus;
import com.techblog.domain.post.model.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findBySlug(String slug);

    List<Post> findByStatusOrderByPublishedAtDesc(ContentStatus status);
}
