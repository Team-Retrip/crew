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
public class RecruitmentAnswerContent {

    private static final int MAX_LENGTH = 500;

    @Column(name = "content", nullable = false, length = MAX_LENGTH)
    private final String value;

    public RecruitmentAnswerContent(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty() || value.length() > MAX_LENGTH) {
            throw new InvalidValueException("답변 내용은 1자 이상 " + MAX_LENGTH + "자 이하로 입력해야 합니다.");
        }
    }
}