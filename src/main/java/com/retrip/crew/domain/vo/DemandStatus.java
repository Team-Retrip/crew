package com.retrip.crew.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DemandStatus {
    PENDING("PENDING", "대기"),
    APPROVED("APPROVED", "승인"),
    REJECTED("REJECTED", "거절"),
    CANCELED("CANCELED", "취소");

    private final String code;
    private final String viewName;

    public static DemandStatus codeOf(String code) {
        return Arrays.stream(DemandStatus.values())
                .filter(participantStatus -> participantStatus.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코드입니다."));
    }
}
