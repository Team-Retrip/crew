package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.vo.DemandStatus;

import java.util.UUID;

public record PendingDemandsResponse(
        UUID crewId,
        UUID demandId,
        UUID memberId,
        DemandStatus status
) {
    public static PendingDemandsResponse of(UUID crewId, Demand demand) {
        return new PendingDemandsResponse(crewId, demand.getId(), demand.getMemberId(), demand.getStatus());
    }
}
