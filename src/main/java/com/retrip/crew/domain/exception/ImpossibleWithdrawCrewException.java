package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.ErrorCode;
import com.retrip.crew.domain.exception.common.IllegalStateException;

public class ImpossibleWithdrawCrewException extends IllegalStateException {
    private static final ErrorCode errorCode = ErrorCode.IMPOSSIBLE_WITHDRAW_CREW;

    public ImpossibleWithdrawCrewException() {
        super(errorCode);
    }

    public ImpossibleWithdrawCrewException(String message) {
        super(errorCode, message);
    }
}
