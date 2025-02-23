package com.retrip.crew.domain.exception;

public class PostTitleException extends CrewException {

    public PostTitleException(String message) {
        super(CrewErrorCode.POST_TITLE_ERROR, message);
    }
}
