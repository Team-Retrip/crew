package com.retrip.crew.application.in.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "참여자가 속한 크루 목록 조회 Response")
public record CrewsOfDemandResponse(
        @Schema(description = "참여 요청자 ID")
        UUID memberId,

        @Schema(description = "크루 ID")
        UUID id,

        @Schema(description = "크루 타이틀")
        String title,

        @Schema(description = "크루 리더 ID")
        UUID leaderId,

        @Schema(description = "크루원 현재 인원수")
        long memberCount,

        @Schema(description = "크루원 최대 인원수")
        int maxMemberCount
) {
}
