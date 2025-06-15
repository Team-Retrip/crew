package com.retrip.crew.application.in.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "참여요청질문 Response")
public record RecruitmentQuestionResponse(
        @Schema(description = "질문 Id") UUID id,
        @Schema(description = "질문 내용") String content
) {}
