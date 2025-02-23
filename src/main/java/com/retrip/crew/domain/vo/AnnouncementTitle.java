package com.retrip.crew.domain.vo;

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
public class AnnouncementTitle {
    private static final int TITLE_LENGTH_LIMIT = 20;

    @Column(name = "title", nullable = false, length = TITLE_LENGTH_LIMIT)
    private final String value;

    public AnnouncementTitle(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value.length() > TITLE_LENGTH_LIMIT) {
            throw new IntroductionTitleException("공지 게시글 제목은 " + TITLE_LENGTH_LIMIT + "자를 넘을 수 없습니다.");
        }
    }
}
