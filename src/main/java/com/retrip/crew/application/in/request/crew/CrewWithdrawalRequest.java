package com.retrip.crew.application.in.request.crew;

import com.retrip.crew.domain.CrewTrip;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "크루 탈퇴 Request")
public record CrewWithdrawalRequest(
        @Schema(description = "크루원 ID")
        UUID memberId,

        @Schema(description = "참여 중인 크루 여행 목록")
        List<CrewTrip> participatingCrewTrips
) {
}
