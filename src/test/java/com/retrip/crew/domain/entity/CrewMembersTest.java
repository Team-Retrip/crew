package com.retrip.crew.domain.entity;

import org.junit.jupiter.api.Test;

import static com.retrip.crew.common.fixture.CrewFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class CrewMembersTest {

    @Test
    void 사용자가_리더이면_true를_반환한다() {
        // given
        Crew crew = createCrewWithMembers(MEMBER_ID);

        // when
        boolean result = crew.getCrewMembers().isLeader(MEMBER_ID);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 사용자가_리더가_아니면_false를_반환한다() {
        // given
        Crew crew = createCrewWithMembers(LEADER_ID);

        // when
        boolean result = crew.getCrewMembers().isLeader(정수_ID);

        // then
        assertThat(result).isFalse();
    }
}
