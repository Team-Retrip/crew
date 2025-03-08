package com.retrip.crew.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class CrewTest {
    @Test
    public void 크루를_생성한다() {
        assertThatCode(() -> Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                UUID.randomUUID())
        ).doesNotThrowAnyException();
    }

}
