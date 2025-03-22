package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.common.IllegalStateException;
import com.retrip.crew.domain.vo.RecruitmentStatus;
import org.junit.jupiter.api.Test;

import static com.retrip.crew.common.fixture.CrewFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

class CrewTest {
    @Test
    void 크루를_생성한다() {
        assertThatCode(() -> Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                LEADER_ID)
        ).doesNotThrowAnyException();
    }

    @Test
    void 크루가_생성되면_모집을_시작한다() {
        // given, when
        Crew crew = createCrew(LEADER_ID);

        // then
        assertThat(crew.getRecruitment().getStatus()).isEqualTo(RecruitmentStatus.RECRUITING);
    }

    @Test
    void 크루_모집을_중지한다() {
        // given
        Crew crew = createCrew(LEADER_ID);

        // when
        crew.stopRecruitment();

        // then
        assertThat(crew.getRecruitment().getStatus()).isEqualTo(RecruitmentStatus.STOPPED);
    }

    @Test
    void 크루_모집을_시작한다() {
        // given
        Crew crew = createCrewWithMembers(LEADER_ID, 100);
        crew.stopRecruitment();

        // when
        crew.startRecruitment();

        // then
        assertThat(crew.getRecruitment().getStatus()).isEqualTo(RecruitmentStatus.RECRUITING);
    }

    @Test
    void 크루_멤버가_최대_모집_인원과_같으면_모집을_시작할_수_없다() {
        // given
        Crew crew = createCrewWithMembers(LEADER_ID, 6);
        crew.stopRecruitment();

        // when, then
        assertThatThrownBy(crew::startRecruitment)
                .isExactlyInstanceOf(IllegalStateException.class);
        assertThat(crew.getRecruitment().getStatus()).isEqualTo(RecruitmentStatus.STOPPED);
    }
}
