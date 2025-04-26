package com.retrip.crew.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.retrip.crew.common.fixture.CrewFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @Test
    void 사용자를_크루_멤버에_추가한다() {
        // given
        Crew crew = createCrew(LEADER_ID);
        Demand demand = new Demand(정수_ID, crew);
        CrewMembers crewMembers = new CrewMembers(crew, LEADER_ID);

        // when
        crewMembers.addMember(demand, crew);

        // then
        List<UUID> ids = crewMembers.getValues().stream().map(CrewMember::getMemberId).toList();
        assertAll(
                () -> assertThat(crewMembers.getSize()).isEqualTo(2),
                () -> assertThat(ids.contains(정수_ID)).isTrue()
        );
    }
}
