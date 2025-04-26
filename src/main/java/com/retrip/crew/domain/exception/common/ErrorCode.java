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
    NOT_CREW_LEADER(BAD_REQUEST, "Crew-002", "크루 리더가 아니면 접근할 수 없습니다."),
    UNABLE_TO_START_RECRUITMENT(BAD_REQUEST, "Crew-003", "크루 멤버를 모집할 수 없습니다."),
    DUPLICATE_DEMAND(BAD_REQUEST, "Crew-004", "이미 크루 참여를 요청한 사용자는 다시 요청할 수 없습니다."),
    ILLEGAL_DEMAND_STATE(BAD_REQUEST, "Crew-004", "이미 크루 참여를 요청한 사용자는 다시 요청할 수 없습니다."),
    INTRODUCTION_NOT_FOUND(BAD_REQUEST, "Crew-005", "크루 자기소개 엔티티를 찾을 수 없습니다."),
    INVALID_ACCESS(FORBIDDEN, "Crew-006","접근 권한이 존재하지 않습니다."),
    CREW_MEMBER_NOT_FOUND(BAD_REQUEST, "Crew-007", "크루원 멤버 엔티티를 찾을 수 없습니다.."),
    POST_NOT_FOUND(BAD_REQUEST, "Crew-008", "자유 게시글 엔티티를 찾을 수 없습니다."),
    POST_UPDATE_FAIL(FORBIDDEN, "Crew-009", "자유 게시글을 수정할 권한이 없습니다."),
    POST_DELETE_FAIL(FORBIDDEN, "Crew-010", "자유 게시글을 삭제할 권한이 없습니다.")
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
