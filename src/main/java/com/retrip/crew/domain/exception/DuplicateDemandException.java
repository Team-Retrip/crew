package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.ErrorCode;
import com.retrip.crew.domain.exception.common.IllegalStateException;

public class DuplicateDemandException extends IllegalStateException {
    private static final ErrorCode errorCode = ErrorCode.DUPLICATE_DEMAND;
    public DuplicateDemandException() {
        super(errorCode);
    }
}
