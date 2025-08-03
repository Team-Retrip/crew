package com.retrip.crew.application.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Schema(description = "참여요청질문 수정 Request")
public record UpdateRecruitmentQuestionRequest(

        @Schema(description = "질문 내용")
        @NotNull
        String content
) {}
