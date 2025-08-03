package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.RecruitmentQuestion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "참여요청질문 수정 Response")
public record UpdateRecruitmentQuestionResponse(
        @Schema(description = "질문 ID")
        UUID id,
        @Schema(description = "질문 내용")
        String content
) {
    public static UpdateRecruitmentQuestionResponse of(RecruitmentQuestion question) {
        return new UpdateRecruitmentQuestionResponse(
                question.getId(),
                question.getContent().getValue()
        );
    }
}
