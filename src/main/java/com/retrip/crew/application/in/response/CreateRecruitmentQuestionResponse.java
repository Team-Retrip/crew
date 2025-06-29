package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.RecruitmentQuestion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "참여요청질문 생성 Response")
public record CreateRecruitmentQuestionResponse(
        @Schema(description = "질문 ID") UUID id,
        @Schema(description = "질문 내용") String content
) {
    public static CreateRecruitmentQuestionResponse of(RecruitmentQuestion question) {
        return new CreateRecruitmentQuestionResponse(
                question.getId(),
                question.getContent().getValue()
        );
    }
}
