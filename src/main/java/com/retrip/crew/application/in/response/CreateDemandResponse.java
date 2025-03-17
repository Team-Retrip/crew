package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Demand;

import java.util.UUID;

public record CreateDemandResponse(
        UUID crewId,
        UUID memberId
) {
    public static CreateDemandResponse of(UUID crewId, Demand demand) {
        return new CreateDemandResponse(crewId, demand.getMemberId());
    }
}
