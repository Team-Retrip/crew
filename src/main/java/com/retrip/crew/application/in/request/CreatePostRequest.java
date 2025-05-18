package com.retrip.crew.application.in.request;

import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;


@Schema(description = "자유 게시글 생성 Request")
public record CreatePostRequest(
        @Schema(description = "게시글 제목")
        @Size(min = 1, max = 30)
        String title,

        @Schema(description = "게시글 내용")
        @Size(min = 1, max = 500)
        String content
) {

    public Post to(Crew crew) {
        return Post.create(title, content, crew);
    }
}
