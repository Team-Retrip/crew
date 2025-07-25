package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.BusinessException;
import com.retrip.crew.domain.exception.common.ErrorCode;

public class CrewMemberExpelFailedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.CREW_MEMBER_EXPEL_FAIL;

    public CrewMemberExpelFailedException(String message) {
        super(errorCode, message);
    }
}