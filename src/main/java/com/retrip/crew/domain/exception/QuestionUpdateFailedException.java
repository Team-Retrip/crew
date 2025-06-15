package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.BusinessException;
import com.retrip.crew.domain.exception.common.ErrorCode;

public class QuestionUpdateFailedException extends BusinessException {
  private static final ErrorCode errorCode = ErrorCode.QUESTION_UPDATE_FAIL;

  public QuestionUpdateFailedException() {
    super(errorCode);
  }
}
