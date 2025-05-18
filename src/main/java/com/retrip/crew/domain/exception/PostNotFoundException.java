package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.EntityNotFoundException;
import com.retrip.crew.domain.exception.common.ErrorCode;

public class PostNotFoundException extends EntityNotFoundException {
    private static final ErrorCode errorCode = ErrorCode.POST_NOT_FOUND;

    public PostNotFoundException() {
        super(errorCode);
    }
}
