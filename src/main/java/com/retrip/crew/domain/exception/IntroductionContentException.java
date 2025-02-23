package com.retrip.crew.domain.exception;

public class IntroductionContentException extends CrewException {
    private static final int ERROR_CODE = 1003;

    public IntroductionContentException(String message) {
        super(CrewErrorCode.INTRODUCTION_CONTENT_ERROR, message);
    }
}
