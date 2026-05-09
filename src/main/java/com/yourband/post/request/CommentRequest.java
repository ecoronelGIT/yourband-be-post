package com.yourband.post.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank(message = "El comentario no puede estar vacío")
    private String content;
}
