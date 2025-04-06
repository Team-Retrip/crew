package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Introduction;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "크루 자기소개 상세 Response")
public record IntroductionDetailResponse(

        @Schema(description = "크루 ID")
        UUID crewId,
        @Schema(description = "자기소개 ID")
        UUID introductionId,
        @Schema(description = "자기소개 등록한 사용자 ID")
        UUID memberId,
        @Schema(description = "자기소개 제목")
        String title,
        @Schema(description = "자기소개 본문")
        String content
) {
    public static IntroductionDetailResponse of(Introduction introduction){
        return IntroductionDetailResponse.builder()
                .crewId(introduction.getCrew().getId())
                .introductionId(introduction.getId())
                .memberId(introduction.getMemberId())
                .title(introduction.getTitle())
                .content(introduction.getContent())
                .build();
    }
}
