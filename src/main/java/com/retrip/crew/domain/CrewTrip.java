package com.retrip.crew.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

import static com.retrip.crew.domain.CrewTrip.CrewTripType.EXCLUSION;

@Schema(description = "크루 여행")
public record CrewTrip(
        @Schema(description = "크루 ID")
        UUID tripId,

        @Schema(description = "크루 여행 타입")
        CrewTripType type
) {
    public boolean isImpossibleWithdrawal() {
        return this.type == EXCLUSION;
    }

    @Getter
    @AllArgsConstructor
    public enum CrewTripType {
        EXCLUSION("EXCLUSION", "크루원 전용"),
        INCLUSION("INCLUSION", "크루원 포함");

        private final String code;
        private final String viewName;
    }
}


