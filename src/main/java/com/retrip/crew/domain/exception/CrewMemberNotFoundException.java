package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.EntityNotFoundException;
import com.retrip.crew.domain.exception.common.ErrorCode;

public class CrewMemberNotFoundException extends EntityNotFoundException {
    private static final ErrorCode errorCode = ErrorCode.CREW_MEMBER_NOT_FOUND;

    public CrewMemberNotFoundException() {
        super(errorCode);
    }
}
