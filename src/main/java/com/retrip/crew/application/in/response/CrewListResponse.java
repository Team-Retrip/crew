package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Crew;
import java.util.UUID;

public record CrewListResponse(
        UUID id,
        String title,
        UUID leaderId,
        Long memberCount,
        Integer maxMemberCount
) {
}
