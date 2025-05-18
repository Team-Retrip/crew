package com.retrip.crew.application.in.request;

import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;


@Schema(description = "자유 게시글 생성 Request")
public record UpdatePostRequest(
        @Schema(description = "게시글 제목")
        @Size(min = 1, max = 30)
        String title,

        @Schema(description = "게시글 내용")
        @Size(min = 1, max = 500)
        String content,

        @Schema(description = "수정자")
        @NotNull
        UUID memberId
) {

}
