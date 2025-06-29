package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.BusinessException;
import com.retrip.crew.domain.exception.common.ErrorCode;

public class QuestionDeleteFailedException extends BusinessException {
    public QuestionDeleteFailedException() {
        super(ErrorCode.QUESTION_DELETE_FAIL);
    }
}