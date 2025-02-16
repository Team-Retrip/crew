package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Crew;

import java.util.UUID;

public record CrewCreateResponse(
        UUID id,
        String name,
        String description,
        UUID leaderId
) {
    public static CrewCreateResponse of(Crew crew) {
        return new CrewCreateResponse(
                crew.getId(),
                crew.getCrewContent().getName(),
                crew.getCrewContent().getDescription(),
                crew.getLeader().getFirst().getId()
        );
    }
}
