package com.retrip.crew.domain.vo;

import com.retrip.crew.domain.exception.common.InvalidValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class PostContent {
    private static final int CONTENT_LENGTH_LIMIT = 200;

    @Column(name = "content", nullable = false, length = CONTENT_LENGTH_LIMIT)
    private final String value;

    public PostContent(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value.length() > CONTENT_LENGTH_LIMIT) {
            throw new InvalidValueException("자유 게시판 내용은 " + CONTENT_LENGTH_LIMIT + "자를 넘을 수 없습니다.");
        }
    }
}
