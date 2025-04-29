package com.retrip.crew.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

import static com.retrip.crew.domain.CrewTrip.CrewTripType.EXCLUSION;

public record CrewTrip(
        UUID tripId,
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


