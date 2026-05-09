package com.yourband.post.service;

import com.yourband.post.client.BandClient;
import com.yourband.post.mapper.PostMapper;
import com.yourband.post.model.Post;
import com.yourband.post.repository.PostCommentRepository;
import com.yourband.post.repository.PostLikeRepository;
import com.yourband.post.repository.PostRepository;
import com.yourband.post.request.PostRequest;
import com.yourband.post.response.PageResponse;
import com.yourband.post.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository likeRepository;
    private final PostCommentRepository commentRepository;
    private final PostMapper postMapper;
    private final BandClient bandClient;

    @Transactional
    public PostResponse create(PostRequest request, UUID authorId) {
        Post post = Post.builder()
                .bandId(request.getBandId())
                .authorId(authorId)
                .content(request.getContent())
                .videoUrl(request.getVideoUrl())
                .audioUrl(request.getAudioUrl())
                .imageUrl(request.getImageUrl())
                .build();

        post = postRepository.save(post);
        return toResponse(post, authorId);
    }

    public PostResponse getById(UUID id, UUID requesterId) {
        Post post = findById(id);
        return toResponse(post, requesterId);
    }

    public PageResponse<PostResponse> getByBand(UUID bandId, UUID requesterId, int page, int size) {
        Page<Post> posts = postRepository.findByBandIdOrderByCreatedAtDesc(
                bandId, PageRequest.of(page, size));
        return toPageResponse(posts, requesterId);
    }

    /** Feed: posts de las bandas que sigue el usuario */
    public PageResponse<PostResponse> getFeed(UUID userId, int page, int size) {
        List<UUID> followedBandIds = bandClient.getFollowedBandIds(userId);

        if (followedBandIds.isEmpty()) {
            return PageResponse.<PostResponse>builder()
                    .content(Collections.emptyList())
                    .page(page).size(size)
                    .totalElements(0).totalPages(0).last(true)
                    .build();
        }

        Page<Post> posts = postRepository.findByBandIdInOrderByCreatedAtDesc(
                followedBandIds, PageRequest.of(page, size));
        return toPageResponse(posts, userId);
    }

    @Transactional
    public void delete(UUID id, UUID requesterId) {
        Post post = findById(id);
        if (!post.getAuthorId().equals(requesterId)) {
            throw new IllegalStateException("Solo el autor puede eliminar esta publicación");
        }
        postRepository.delete(post);
    }

    private Post findById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post no encontrado: " + id));
    }

    private PostResponse toResponse(Post post, UUID requesterId) {
        long likes    = likeRepository.countByPostId(post.getId());
        long comments = commentRepository.countByPostId(post.getId());
        boolean likedByMe = requesterId != null &&
                likeRepository.existsByPostIdAndUserId(post.getId(), requesterId);
        return postMapper.toResponse(post, likes, comments, likedByMe);
    }

    private PageResponse<PostResponse> toPageResponse(Page<Post> page, UUID requesterId) {
        List<PostResponse> content = page.getContent().stream()
                .map(p -> toResponse(p, requesterId))
                .toList();
        return PageResponse.<PostResponse>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
