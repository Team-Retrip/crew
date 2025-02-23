package com.retrip.crew.domain.exception;

public class AnnouncementContentException extends CrewException {

    public AnnouncementContentException(String message) {
        super(CrewErrorCode.ANNOUNCEMENT_CONTENT_ERROR, message);
    }
}
