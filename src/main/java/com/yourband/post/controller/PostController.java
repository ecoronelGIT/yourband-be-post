package com.yourband.post.controller;

import com.yourband.post.request.PostRequest;
import com.yourband.post.response.PageResponse;
import com.yourband.post.response.PostResponse;
import com.yourband.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(
            @Valid @RequestBody PostRequest request,
            @RequestHeader("X-User-Id") UUID authorId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.create(request, authorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID requesterId) {
        return ResponseEntity.ok(postService.getById(id, requesterId));
    }

    @GetMapping("/band/{bandId}")
    public ResponseEntity<PageResponse<PostResponse>> getByBand(
            @PathVariable UUID bandId,
            @RequestHeader("X-User-Id") UUID requesterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(postService.getByBand(bandId, requesterId, page, size));
    }

    @GetMapping("/feed")
    public ResponseEntity<PageResponse<PostResponse>> getFeed(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(postService.getFeed(userId, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID requesterId) {
        postService.delete(id, requesterId);
        return ResponseEntity.noContent().build();
    }
}
