package com.retrip.crew.application.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Schema(description = "참여요청질문 생성 Request")
public record CreateRecruitmentQuestionRequest(
        @Schema(description = "질문 내용")
        @NotNull
        @Size(min = 1, max = 100)
        String content,
        @Schema(description = "작성자")
        @NotNull
        UUID memberId
) {}
