package com.retrip.crew;

import com.retrip.crew.domain.entity.Crew;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;

class CrewApplicationTests {
    @DisplayName("제목을 입력해 크루를 생성할 수 있다.")
    @Test
    void create() {
        assertThatCode(() -> Crew.create("속초 크루 모집")).doesNotThrowAnyException();
    }

}
