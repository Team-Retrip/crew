package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Crew;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "크루 생성 Response")
public record CrewCreateResponse(
        @Schema(description = "크루 ID")
        UUID id,
        @Schema(description = "크루 타이틀")
        String title,
        @Schema(description = "크루 설명")
        String description,
        @Schema(description = "리더 ID")
        UUID leaderId
) {
    public static CrewCreateResponse of(Crew crew) {
        return new CrewCreateResponse(
                crew.getId(),
                crew.getTitle().getValue(),
                crew.getDescription(),
                crew.getLeader().getId()
        );
    }
}
