package com.retrip.crew.domain.vo;

import com.retrip.crew.domain.exception.common.ErrorCode;
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
public class QuestionContent {

    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 100;

    @Column(name = "content", nullable = false, length = MAX_LENGTH)
    private final String value;

    public QuestionContent(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.trim().length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new InvalidValueException(ErrorCode.INVALID_QUESTION_LENGTH);
        }
    }
}
