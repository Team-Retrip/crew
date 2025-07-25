package com.retrip.crew.application.in.request.crew;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "크루원 추방 Request")
public record CrewExpelRequest(
        @Schema(description = "크루 리더 ID")
        UUID leaderId,

        @Schema(description = "추방할 크루원 ID")
        UUID memberId
) {
}