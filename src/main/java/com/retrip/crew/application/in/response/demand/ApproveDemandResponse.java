package com.retrip.crew.application.in.response.demand;

import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.vo.DemandStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "크루 참여 요청 승인 Response")
public record ApproveDemandResponse(
        @Schema(description = "크루 ID")
        UUID crewId,

        @Schema(description = "참여 요청 ID")
        UUID demandId,

        @Schema(description = "참여 요청자 ID")
        UUID memberId,

        @Schema(description = "참여 요청 상태")
        DemandStatus status
) {
    public static ApproveDemandResponse of(Demand demand) {
        return new ApproveDemandResponse(demand.getCrew().getId(), demand.getId(), demand.getMemberId(), demand.getStatus());
    }
}
