package com.yourband.post.repository;

import com.yourband.post.model.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostCommentRepository extends JpaRepository<PostComment, UUID> {

    Page<PostComment> findByPostIdOrderByCreatedAtDesc(UUID postId, Pageable pageable);

    long countByPostId(UUID postId);
}
