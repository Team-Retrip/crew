package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.BusinessException;
import com.retrip.crew.domain.exception.common.ErrorCode;

public class PostUpdateFailedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.POST_UPDATE_FAIL;

    public PostUpdateFailedException() {
        super(errorCode);
    }
}
