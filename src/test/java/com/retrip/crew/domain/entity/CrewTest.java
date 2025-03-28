package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.common.IllegalStateException;
import com.retrip.crew.domain.vo.RecruitmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class CrewTest {
    @Test
    void 크루를_생성한다() {
        assertThatCode(() -> Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                UUID.randomUUID(),
                List.of("질문1"))

        ).doesNotThrowAnyException();
    }

    @Test
    void 크루가_생성되면_모집을_시작한다() {
        // given, when
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                UUID.randomUUID(),
                List.of("질문1")
                );

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
                UUID.randomUUID(),
                List.of("질문1"));

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
                UUID.randomUUID(),
                List.of("질문1"));
        crew.stopRecruitment();

        // when
        crew.startRecruitment();

        // then
        assertThat(crew.getRecruitment().getStatus()).isEqualTo(RecruitmentStatus.RECRUITING);
    }

    @Test
    void 크루_멤버가_최대_모집_인원과_같으면_모집을_시작할_수_없다() {
        // given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                5,
                UUID.randomUUID(),
                List.of("질문1"));
        List<CrewMember> crewMemberList = List.of(
                new CrewMember(crew, UUID.randomUUID(), CrewMemberRole.LEADER),
                new CrewMember(crew, UUID.randomUUID(), CrewMemberRole.PARTICIPANT),
                new CrewMember(crew, UUID.randomUUID(), CrewMemberRole.PARTICIPANT),
                new CrewMember(crew, UUID.randomUUID(), CrewMemberRole.PARTICIPANT),
                new CrewMember(crew, UUID.randomUUID(), CrewMemberRole.PARTICIPANT)
        );
        crew.stopRecruitment();
        CrewMembers crewMembers = new CrewMembers(crew, UUID.randomUUID());
        ReflectionTestUtils.setField(crewMembers, "values", crewMemberList);
        ReflectionTestUtils.setField(crew, "crewMembers", crewMembers);

        // when, then
        assertThatThrownBy(crew::startRecruitment)
                .isExactlyInstanceOf(IllegalStateException.class);
        assertThat(crew.getRecruitment().getStatus()).isEqualTo(RecruitmentStatus.STOPPED);
    }
}
