package com.yourband.post.service;

import com.yourband.post.client.NotificationClient;
import com.yourband.post.model.PostLike;
import com.yourband.post.repository.PostLikeRepository;
import com.yourband.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository likeRepository;
    private final PostRepository postRepository;
    private final NotificationClient notificationClient;

    @Transactional
    public long like(UUID postId, UUID userId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post no encontrado: " + postId));

        if (!likeRepository.existsByPostIdAndUserId(postId, userId)) {
            likeRepository.save(PostLike.builder()
                    .postId(postId)
                    .userId(userId)
                    .build());

            notificationClient.send(
                    post.getAuthorId(),
                    "POST_LIKED",
                    userId,
                    postId,
                    "POST",
                    "A alguien le gustó tu publicación"
            );
        }
        return likeRepository.countByPostId(postId);
    }

    @Transactional
    public long unlike(UUID postId, UUID userId) {
        likeRepository.findByPostIdAndUserId(postId, userId)
                .ifPresent(likeRepository::delete);
        return likeRepository.countByPostId(postId);
    }

    public long getCount(UUID postId) {
        return likeRepository.countByPostId(postId);
    }
}
