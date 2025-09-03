package com.retrip.crew.application.in.request.demand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Schema(description = "크루 참여 요청 수정 Request")
public record UpdateDemandRequest(
        @Schema(description = "참여 요청자 ID")
        @NotNull
        UUID memberId,

        @Schema(description = "수정할 질문 답변 목록")
        @Valid
        @NotNull
        List<AnswerRequest> answers
) {
    @Schema(description = "질문 답변 Request")
    public record AnswerRequest(
            @Schema(description = "질문 ID")
            @NotNull
            UUID questionId,

            @Schema(description = "새로운 답변 내용")
            @NotNull
            String content
    ) {}
}