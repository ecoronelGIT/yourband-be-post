package com.yourband.post.controller;

import com.yourband.post.request.CommentRequest;
import com.yourband.post.response.CommentResponse;
import com.yourband.post.response.PageResponse;
import com.yourband.post.service.PostCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> add(
            @PathVariable UUID postId,
            @Valid @RequestBody CommentRequest request,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.add(postId, request, userId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<CommentResponse>> getByPost(
            @PathVariable UUID postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(commentService.getByPost(postId, page, size));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID postId,
            @PathVariable UUID commentId,
            @RequestHeader("X-User-Id") UUID requesterId) {
        commentService.delete(commentId, requesterId);
        return ResponseEntity.noContent().build();
    }
}
