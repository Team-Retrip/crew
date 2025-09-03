package com.retrip.crew.application.in.response.demand;

import com.retrip.crew.domain.entity.Demand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "크루 참여 요청 수정 Response")
public record UpdateDemandResponse(
        @Schema(description = "크루 ID")
        UUID crewId,

        @Schema(description = "참여 요청 ID")
        UUID demandId
) {
    public static UpdateDemandResponse from(Demand demand) {
        return UpdateDemandResponse.builder()
                .crewId(demand.getCrew().getId())
                .demandId(demand.getId())
                .build();
    }
}