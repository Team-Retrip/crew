package com.retrip.crew.domain.exception.common;

public class InvalidAccessException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.INVALID_ACCESS;

    public InvalidAccessException() {
        super(errorCode);
    }
    public InvalidAccessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidAccessException(String message) {
        super(errorCode, message);
    }

    public InvalidAccessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
