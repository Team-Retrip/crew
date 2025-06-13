package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.BusinessException;
import com.retrip.crew.domain.exception.common.ErrorCode;

public class PostDeleteFailedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.POST_DELETE_FAIL;

    public PostDeleteFailedException() {
        super(errorCode);
    }
}
