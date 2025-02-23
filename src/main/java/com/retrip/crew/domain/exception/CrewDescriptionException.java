package com.retrip.crew.domain.exception;

public class CrewDescriptionException extends CrewException {

    public CrewDescriptionException(String message) {
        super(CrewErrorCode.CREW_DESCRIPTION_ERROR, message);
    }
}
