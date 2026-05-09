package com.yourband.post.service;

import com.yourband.post.client.NotificationClient;
import com.yourband.post.mapper.PostMapper;
import com.yourband.post.model.PostComment;
import com.yourband.post.repository.PostCommentRepository;
import com.yourband.post.repository.PostRepository;
import com.yourband.post.request.CommentRequest;
import com.yourband.post.response.CommentResponse;
import com.yourband.post.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final NotificationClient notificationClient;

    @Transactional
    public CommentResponse add(UUID postId, CommentRequest request, UUID userId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post no encontrado: " + postId));

        PostComment comment = PostComment.builder()
                .postId(postId)
                .userId(userId)
                .content(request.getContent())
                .build();

        CommentResponse saved = postMapper.toCommentResponse(commentRepository.save(comment));

        notificationClient.send(
                post.getAuthorId(),
                "POST_COMMENTED",
                userId,
                postId,
                "POST",
                "Alguien comentó tu publicación"
        );

        return saved;
    }

    public PageResponse<CommentResponse> getByPost(UUID postId, int page, int size) {
        Page<PostComment> comments = commentRepository
                .findByPostIdOrderByCreatedAtDesc(postId, PageRequest.of(page, size));

        return PageResponse.<CommentResponse>builder()
                .content(comments.getContent().stream().map(postMapper::toCommentResponse).toList())
                .page(comments.getNumber())
                .size(comments.getSize())
                .totalElements(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .last(comments.isLast())
                .build();
    }

    @Transactional
    public void delete(UUID commentId, UUID requesterId) {
        PostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado: " + commentId));

        if (!comment.getUserId().equals(requesterId)) {
            throw new IllegalStateException("Solo el autor puede eliminar este comentario");
        }
        commentRepository.delete(comment);
    }
}
