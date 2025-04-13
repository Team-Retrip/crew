package com.retrip.crew.domain.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "Common-001", "Server error"),
    INVALID_INPUT_VALUE(BAD_REQUEST, "Common-002", "Invalid input value"),
    HANDLE_ACCESS_DENIED(FORBIDDEN, "Common-003", "Access is denied"),
    ENTITY_NOT_FOUND(BAD_REQUEST, "Common-004", "Entity not found"),
    ILLEGAL_STATE(BAD_REQUEST, "Common-005", "Illegal state"),


    CREW_NOT_FOUND(BAD_REQUEST, "Crew-001", "크루 엔티티를 찾을 수 없습니다."),
    INVALID_QUESTION_COUNT(BAD_REQUEST, "Crew-002", "질문은 최대 10개까지 입력할 수 있습니다."),
    INVALID_QUESTION_LENGTH(BAD_REQUEST, "Crew-003", "각 질문은 10자 이상 200자 이하로 입력해야 합니다.")

            ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
