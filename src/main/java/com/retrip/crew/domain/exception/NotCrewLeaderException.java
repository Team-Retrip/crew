package com.retrip.crew.domain.exception;

import com.retrip.crew.domain.exception.common.ErrorCode;
import com.retrip.crew.domain.exception.common.InvalidValueException;

public class NotCrewLeaderException extends InvalidValueException {
    private static final ErrorCode errorCode = ErrorCode.NOT_CREW_LEADER;

    public NotCrewLeaderException() {
        super(errorCode);
    }
}
