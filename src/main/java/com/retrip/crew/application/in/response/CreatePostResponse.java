package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CreatePostResponse(
        @Schema(description = "자유 게시글 ID")
        UUID id,

        @Schema(description = "작성자")
        UUID createAt,

        @Schema(description = "게시글 제목")
        String title,

        @Schema(description = "게시글 내용")
        String content
) {
    public static CreatePostResponse of(Post post) {
        return new CreatePostResponse(
                post.getId(),
                UUID.randomUUID(), //todo: CreateAt 생성시, 변경 예정
                post.getTitle().getValue(),
                post.getContent().getValue()
        );
    }
}
