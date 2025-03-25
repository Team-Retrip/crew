package com.retrip.crew.application.in.response.crew;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "크루 리스트 조회 Response")
public record CrewListResponse(
        @Schema(description = "크루 ID")
        UUID id,

        @Schema(description = "크루 타이틀")
        String title,

        @Schema(description = "크루 리더 ID")
        UUID leaderId,

        @Schema(description = "크루원 현재 인원수")
        Long memberCount,

        @Schema(description = "크루원 수용가능한 최대 인원수")
        Integer maxMemberCount
) {
}
