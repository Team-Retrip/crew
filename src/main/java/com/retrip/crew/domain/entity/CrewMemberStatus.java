package com.retrip.crew.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CrewMemberStatus {
    ACTIVE("ACTIVE", "활동 중"),
    EXPELLED("EXPELLED", "추방됨");

    private final String code;
    private final String viewName;
}