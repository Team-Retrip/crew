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
public class CrewDescription {
    private static final int DESCRIPTION_LENGTH_LIMIT = 200;

    @Column(name = "description", nullable = false, length = DESCRIPTION_LENGTH_LIMIT)
    private final String value;

    public CrewDescription(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value.length() > DESCRIPTION_LENGTH_LIMIT) {
            throw new InvalidValueException("크루 상세 설명은 " + DESCRIPTION_LENGTH_LIMIT + "자를 넘을 수 없습니다.");
        }
    }
}
