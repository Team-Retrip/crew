package com.retrip.crew.application.in.response.demand;

import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.entity.RecruitmentAnswer;
import com.retrip.crew.domain.vo.DemandStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Schema(description = "내 참여 요청 조회 Response")
public record MyDemandResponse(
        @Schema(description = "크루 ID")
        UUID crewId,

        @Schema(description = "참여 요청 ID")
        UUID demandId,

        @Schema(description = "참여 요청 상태")
        DemandStatus status,

        @Schema(description = "질문 답변 목록")
        List<AnswerResponse> answers
) {
    public static MyDemandResponse from(Demand demand) {
        return MyDemandResponse.builder()
                .crewId(demand.getCrew().getId())
                .demandId(demand.getId())
                .status(demand.getStatus())
                .answers(demand.getAnswers().stream()
                        .map(AnswerResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Schema(description = "질문 답변 Response")
    public record AnswerResponse(
            @Schema(description = "질문 ID")
            UUID questionId,
            @Schema(description = "질문 내용")
            String questionContent,
            @Schema(description = "답변 내용")
            String answerContent
    ) {
        public static AnswerResponse from(RecruitmentAnswer answer) {
            return new AnswerResponse(
                    answer.getQuestion().getId(),
                    answer.getQuestion().getContent().getValue(),
                    answer.getContent().getValue()
            );
        }
    }
}