package com.retrip.crew.domain.vo;

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
public class SelfIntroduceBoardContent {
    private static final int TITLE_LENGTH_LIMIT = 30;
    private static final int CONTENT_LENGTH_LIMIT = 100;

    @Column(name = "title", nullable = false, length = TITLE_LENGTH_LIMIT)
    private final String title;

    @Column(name = "content", nullable = false, length = CONTENT_LENGTH_LIMIT)
    private final String content;

    public SelfIntroduceBoardContent(String title, String content) {
        validate(title, content);
        this.title = title;
        this.content = content;
    }

    private void validate(String title, String content) {
        if (title.length() > TITLE_LENGTH_LIMIT) {
            throw new IllegalArgumentException("자기 소개 게시글 제목은 " + TITLE_LENGTH_LIMIT + "자를 넘을 수 없습니다.");
        }
        if (content.length() > CONTENT_LENGTH_LIMIT) {
            throw new IllegalArgumentException("자기 소개 게시글 내용은 " + CONTENT_LENGTH_LIMIT + "자를 넘을 수 없습니다.");
        }
    }
}
