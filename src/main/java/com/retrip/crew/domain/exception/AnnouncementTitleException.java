package com.retrip.crew.domain.exception;

public class AnnouncementTitleException extends CrewException {

    public AnnouncementTitleException(String message) {
        super(CrewErrorCode.ANNOUNCEMENT_TITLE_ERROR, message);
    }
}
