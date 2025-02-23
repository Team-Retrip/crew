package com.retrip.crew.domain.exception;

public enum CrewErrorCode {
    CREW_TITLE_ERROR(1001),
    CREW_DESCRIPTION_ERROR(1002),
    POST_TITLE_ERROR(1003),
    POST_CONTENT_ERROR(1004),
    ANNOUNCEMENT_TITLE_ERROR(1005),
    ANNOUNCEMENT_CONTENT_ERROR(1006),
    INTRODUCTION_TITLE_ERROR(1007),
    INTRODUCTION_CONTENT_ERROR(1008);

    private final int code;
    CrewErrorCode(int code) {
        this.code = code;
    }
}
