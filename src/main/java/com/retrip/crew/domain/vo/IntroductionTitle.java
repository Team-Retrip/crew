package com.retrip.crew.domain.vo;

import com.retrip.crew.domain.exception.CrewTitleException;
import com.retrip.crew.domain.exception.IntroductionTitleException;
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
public class IntroductionTitle {
    private static final int TITLE_LENGTH_LIMIT = 20;

    @Column(name = "title", nullable = false, length = TITLE_LENGTH_LIMIT)
    private final String value;

    public IntroductionTitle(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value.length() > TITLE_LENGTH_LIMIT) {
            throw new IntroductionTitleException("자기 소개 게시글 제목은 " + TITLE_LENGTH_LIMIT + "자를 넘을 수 없습니다.");
        }
    }
}
