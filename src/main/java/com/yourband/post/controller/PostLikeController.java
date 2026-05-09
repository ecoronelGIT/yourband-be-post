package com.yourband.post.controller;

import com.yourband.post.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/posts/{postId}/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService likeService;

    @PostMapping
    public ResponseEntity<Long> like(
            @PathVariable UUID postId,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(likeService.like(postId, userId));
    }

    @DeleteMapping
    public ResponseEntity<Long> unlike(
            @PathVariable UUID postId,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(likeService.unlike(postId, userId));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCount(@PathVariable UUID postId) {
        return ResponseEntity.ok(likeService.getCount(postId));
    }
}
