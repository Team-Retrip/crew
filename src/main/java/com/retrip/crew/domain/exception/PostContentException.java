package com.retrip.crew.domain.exception;

public class PostContentException extends CrewException {

    public PostContentException(String message) {
        super(CrewErrorCode.POST_CONTENT_ERROR, message);
    }
}
