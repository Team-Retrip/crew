package com.retrip.crew.domain.exception;

public abstract class CrewException extends RuntimeException {
    private final CrewErrorCode errorCode;

    public CrewException(CrewErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
