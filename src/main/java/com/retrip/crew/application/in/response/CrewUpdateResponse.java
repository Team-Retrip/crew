package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.vo.RecruitmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CrewUpdateResponse(
        @Schema(description = "크루 ID")
        UUID id,

        @Schema(description = "크루 타이틀")
        String title,

        @Schema(description = "크루 설명")
        String description,

        @Schema(description = "리더 ID")
        UUID leaderId,

        @Schema(description = "크루 최대 인원수")
        int maxMembers,

        @Schema(description = "모집 상태")
        RecruitmentStatus status
) {
    public static CrewUpdateResponse of(Crew crew) {
        return new CrewUpdateResponse(
                crew.getId(),
                crew.getTitle().getValue(),
                crew.getDescription(),
                crew.getLeader().getId(),
                crew.getRecruitment().getMaxMembers(),
                crew.getRecruitment().getStatus()
        );
    }
}
