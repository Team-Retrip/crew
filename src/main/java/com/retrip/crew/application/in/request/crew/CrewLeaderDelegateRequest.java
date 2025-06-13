package com.retrip.crew.application.in.request.crew;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "크루 리더 위임 Request")
public record CrewLeaderDelegateRequest(
        @Schema(description = "리더 ID")
        UUID leaderId,

        @Schema(description = "위임 리더 ID")
        UUID newLeaderId
) {
}
