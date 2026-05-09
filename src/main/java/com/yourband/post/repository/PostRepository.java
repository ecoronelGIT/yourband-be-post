package com.yourband.post.repository;

import com.yourband.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    Page<Post> findByBandIdOrderByCreatedAtDesc(UUID bandId, Pageable pageable);

    /** Feed: posts de las bandas seguidas, ordenados por fecha desc */
    Page<Post> findByBandIdInOrderByCreatedAtDesc(List<UUID> bandIds, Pageable pageable);
}
