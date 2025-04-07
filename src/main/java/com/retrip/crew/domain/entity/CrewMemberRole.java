package com.retrip.crew.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CrewMemberRole {
    LEADER("LEADER", "리더"),
    PARTICIPANT("PARTICIPANT", "참가자");

    private final String code;
    private final String viewName;

    public static CrewMemberRole codeOf(String code) {
        return Arrays.stream(CrewMemberRole.values())
                .filter(crewMemberRole -> crewMemberRole.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코드입니다."));
    }

    public static boolean isLeaderRole(CrewMemberRole role) {
        return LEADER == role;
    }
}

