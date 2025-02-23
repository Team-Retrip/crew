package com.retrip.crew.domain.vo;

import com.retrip.crew.domain.exception.CrewDescriptionException;
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
public class IntroductionContent {
    private static final int CONTENT_LENGTH_LIMIT = 500;

    @Column(name = "content", nullable = false, length = CONTENT_LENGTH_LIMIT)
    private final String value;

    public IntroductionContent(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value.length() > CONTENT_LENGTH_LIMIT) {
            throw new CrewDescriptionException("자기 소개 게시글 내용은 " + CONTENT_LENGTH_LIMIT + "자를 넘을 수 없습니다.");
        }
    }
}
