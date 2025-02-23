package com.retrip.crew.domain.exception;

public class CrewTitleException extends CrewException {
    public CrewTitleException(String message) {
        super(CrewErrorCode.CREW_TITLE_ERROR, message);
    }
}
