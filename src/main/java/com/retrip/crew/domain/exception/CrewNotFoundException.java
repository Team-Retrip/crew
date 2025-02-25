package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.EntityNotFoundException;
import com.retrip.crew.domain.exception.common.ErrorCode;

public class CrewNotFoundException extends EntityNotFoundException {
    private static final ErrorCode errorCode = ErrorCode.CREW_NOT_FOUNT;

    public CrewNotFoundException() {
        super(errorCode);
    }
}
