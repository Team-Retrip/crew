package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.ErrorCode;
import com.retrip.crew.domain.exception.common.IllegalStateException;

public class IllegalDemandStateException extends IllegalStateException {
    private static final ErrorCode errorCode = ErrorCode.ILLEGAL_DEMAND_STATE;
    public IllegalDemandStateException(String message) {
        super(errorCode, message);
    }
}
