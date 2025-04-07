package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.ErrorCode;
import com.retrip.crew.domain.exception.common.IllegalStateException;

public class UnableToStartRecruitmentException extends IllegalStateException {
    private static final ErrorCode errorCode = ErrorCode.UNABLE_TO_START_RECRUITMENT;
    public UnableToStartRecruitmentException(String message) {
        super(errorCode, message);
    }
}
