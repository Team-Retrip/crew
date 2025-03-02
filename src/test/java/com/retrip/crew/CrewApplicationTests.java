package com.retrip.crew;

import com.retrip.crew.domain.entity.Crew;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;

class CrewApplicationTests {
    @DisplayName("제목을 입력해 크루를 생성할 수 있다.")
    @Test
    void create() {
        assertThatCode(() -> Crew.create(
                "인천 크루 모집",
                "인천 크루원을 모집합니다.\n 가입나이: 20~29살 \n 금지 사항\n - 연애 금지\n - 만남 당일 취소 금지\n - 사적 연락 금지",
                4,
                UUID.randomUUID()
        )).doesNotThrowAnyException();
    }

}
