package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.vo.RecruitmentStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class CrewTest {
    @Test
    void 크루를_생성한다() {
        assertThatCode(() -> Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                UUID.randomUUID())
        ).doesNotThrowAnyException();
    }

    @Test
    void 크루가_생성되면_모집을_시작한다() {
        // given, when
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                UUID.randomUUID());

        // then
        assertThat(crew.getRecruitment().getStatus()).isEqualTo(RecruitmentStatus.RECRUITING);
    }

    @Test
    void 크루_모집을_중지한다() {
        // given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                UUID.randomUUID());

        // when
        crew.stopRecruitment();

        // then
        assertThat(crew.getRecruitment().getStatus()).isEqualTo(RecruitmentStatus.STOPPED);
    }

    @Test
    void 크루_모집을_시작한다() {
        // given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                UUID.randomUUID());
        crew.stopRecruitment();

        // when
        crew.startRecruitment();

        // then
        assertThat(crew.getRecruitment().getStatus()).isEqualTo(RecruitmentStatus.RECRUITING);
    }
}
