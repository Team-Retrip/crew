package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Introduction;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "크루 자기소개 수정 Response")
public record IntroductionUpdateResponse(

        @Schema(description = "크루 ID")
        UUID crewId,
        @Schema(description = "자기소개 ID")
        UUID introductionId,
        @Schema(description = "자기소개 제목")
        String title,
        @Schema(description = "자기소개 본문")
        String content
) {
    public static IntroductionUpdateResponse from(Introduction introduction){
        return new IntroductionUpdateResponse(
                introduction.getCrew().getId(),
                introduction.getId(),
                introduction.getTitle(),
                introduction.getContent()
        );
    }
}
