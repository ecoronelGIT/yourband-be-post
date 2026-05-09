package com.yourband.post.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PostResponse {
    private UUID id;
    private UUID bandId;
    private UUID authorId;
    private String content;
    private String videoUrl;
    private String audioUrl;
    private String imageUrl;
    private long likeCount;
    private long commentCount;
    private boolean likedByMe;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
