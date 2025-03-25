package com.retrip.crew.application.in.response.demand;

import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.vo.RecruitmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "크루 모집 상태 변경 Response")
public record ChangeRecruitmentStatusResponse(
        @Schema(description = "크루 ID")
        UUID id,

        @Schema(description = "모집 상태")
        RecruitmentStatus status
) {
    public static ChangeRecruitmentStatusResponse of(Crew crew) {
        return new ChangeRecruitmentStatusResponse(crew.getId(), crew.getRecruitment().getStatus());
    }
}
