package com.retrip.crew.application.in.request.demand;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "크루 참여 요청 생성 Request")
public record CreateDemandRequest(
        UUID memberId
) {
}
