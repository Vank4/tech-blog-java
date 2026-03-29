package com.techblog.domain.post.repository;

import com.techblog.domain.post.model.Post;
import com.techblog.domain.post.model.PostModerationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostModerationLogRepository extends JpaRepository<PostModerationLog, Long> {


    List<PostModerationLog> findAllByOrderByModeratedAtDesc();


    Optional<PostModerationLog> findTopByPostOrderByModeratedAtDesc(Post post);

}