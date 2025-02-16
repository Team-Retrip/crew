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
public class CrewContent {
    private static final int NAME_LENGTH_LIMIT = 30;
    private static final int DESCRIPTION_LENGTH_LIMIT = 100;

    @Column(name = "name", nullable = false, length = NAME_LENGTH_LIMIT)
    private final String name;

    @Column(name = "description", nullable = false, length = DESCRIPTION_LENGTH_LIMIT)
    private final String description;

    public CrewContent(String name, String description) {
        validate(name, description);
        this.name = name;
        this.description = description;
    }

    private void validate(String name, String description) {
        if (name.length() > NAME_LENGTH_LIMIT) {
            throw new IllegalArgumentException("크루 제목은 " + NAME_LENGTH_LIMIT + "자를 넘을 수 없습니다.");
        }
        if (description.length() > DESCRIPTION_LENGTH_LIMIT) {
            throw new IllegalArgumentException("크루 설명은 " + DESCRIPTION_LENGTH_LIMIT + "자를 넘을 수 없습니다.");
        }
    }
}
