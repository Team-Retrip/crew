package com.retrip.crew.application.in.response.crew;

import com.retrip.crew.domain.entity.CrewMember;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "크루 리더 위임 Response")
public record CrewLeaderDelegateResponse(
        @Schema(description = "리더 ID")
        UUID leaderId,

        @Schema(description = "크루원 역할 코드")
        String roleCode,

        @Schema(description = "크루원 역할명")
        String roleName
) {
    public static CrewLeaderDelegateResponse of(CrewMember newLeader) {
        return new CrewLeaderDelegateResponse(
                newLeader.getMemberId(),
                newLeader.getCrewMemberRole().getCode(),
                newLeader.getCrewMemberRole().getViewName()
        );
    }
}
