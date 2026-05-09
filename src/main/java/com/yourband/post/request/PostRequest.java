package com.yourband.post.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PostRequest {

    @NotNull(message = "El bandId es obligatorio")
    private UUID bandId;

    private String content;

    private String videoUrl;

    private String audioUrl;

    private String imageUrl;
}
