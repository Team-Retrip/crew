package com.retrip.crew.application.in.response.demand;

import com.retrip.crew.domain.entity.Demand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "크루 참여 요청 생성 Response")
public record CreateDemandResponse(
        @Schema(description = "크루 ID")
        UUID crewId,

        @Schema(description = "참여 요청자 ID")
        UUID memberId
) {
    public static CreateDemandResponse of(UUID crewId, Demand demand) {
        return new CreateDemandResponse(crewId, demand.getMemberId());
    }
}
