package com.retrip.crew.application.in.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "크루 자기소개 리스트 Response")
public record IntroductionListResponse(

        @Schema(description = "크루 ID")
        UUID crewId,
        @Schema(description = "자기소개 ID")
        UUID introductionId,
        @Schema(description = "자기소개 제목")
        String title,
        @Schema(description = "자기소개 등록일자")
        LocalDateTime createdAt,
        @Schema(description = "자기소개 수정일자")
        LocalDateTime editedAt
) {
}
