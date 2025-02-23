package com.retrip.crew.domain.exception;

public class IntroductionTitleException extends CrewException {
    private static final int ERROR_CODE = 1003;

    public IntroductionTitleException(String message) {
        super(CrewErrorCode.INTRODUCTION_TITLE_ERROR, message);
    }
}
