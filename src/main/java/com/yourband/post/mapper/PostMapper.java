package com.yourband.post.mapper;

import com.yourband.post.model.Post;
import com.yourband.post.model.PostComment;
import com.yourband.post.response.CommentResponse;
import com.yourband.post.response.PostResponse;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostResponse toResponse(Post post, long likeCount, long commentCount, boolean likedByMe) {
        return PostResponse.builder()
                .id(post.getId())
                .bandId(post.getBandId())
                .authorId(post.getAuthorId())
                .content(post.getContent())
                .videoUrl(post.getVideoUrl())
                .audioUrl(post.getAudioUrl())
                .imageUrl(post.getImageUrl())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .likedByMe(likedByMe)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public CommentResponse toCommentResponse(PostComment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
