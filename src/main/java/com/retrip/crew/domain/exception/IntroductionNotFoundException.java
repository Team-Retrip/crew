package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.EntityNotFoundException;
import com.retrip.crew.domain.exception.common.ErrorCode;

public class IntroductionNotFoundException extends EntityNotFoundException {
    private static final ErrorCode errorCode = ErrorCode.INTRODUCTION_NOT_FOUND;

    public IntroductionNotFoundException() {
        super(errorCode);
    }
}
