package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record DeletePostResponse(
        @Schema(description = "자유 게시글 ID")
        UUID id
) {
    public static DeletePostResponse of(UUID id) {
        return new DeletePostResponse(
                id
        );
    }
}
