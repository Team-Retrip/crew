package com.retrip.crew.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class RecruitmentTest {
    @Test
    void 모집을_생성한다() {
        assertThatCode(() -> new Recruitment(100))
                .doesNotThrowAnyException();
    }
}
