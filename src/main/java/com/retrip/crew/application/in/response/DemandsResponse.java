package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.vo.DemandStatus;

import java.util.UUID;

public record DemandsResponse(
        UUID crewId,
        UUID demandId,
        UUID memberId,
        DemandStatus status
) {
    public static DemandsResponse of(UUID crewId, Demand demand) {
        return new DemandsResponse(crewId, demand.getId(), demand.getMemberId(), demand.getStatus());
    }
}
