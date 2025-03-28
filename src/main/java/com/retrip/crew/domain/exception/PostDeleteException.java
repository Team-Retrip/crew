package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.BusinessException;
import com.retrip.crew.domain.exception.common.EntityNotFoundException;
import com.retrip.crew.domain.exception.common.ErrorCode;

public class PostDeleteException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.POST_DELETE_FAIL;

    public PostDeleteException() {
        super(errorCode);
    }
}
