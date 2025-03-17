package com.retrip.crew.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RecruitmentStatus {
    RECRUITING("RECRUITING", "모집 중"),
    STOPPED("RECRUITMENT_CLOSED", "모집 중지")
    ;

    private final String code;
    private final String viewName;

    public static RecruitmentStatus codeOf(String code) {
        return Arrays.stream(RecruitmentStatus.values())
                .filter(tripStatus -> tripStatus.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코드입니다."));
    }
}
