package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.CrewTrip;
import com.retrip.crew.domain.exception.ImpossibleWithdrawCrewException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.retrip.crew.common.fixture.CrewFixture.*;
import static com.retrip.crew.domain.CrewTrip.CrewTripType.EXCLUSION;
import static com.retrip.crew.domain.CrewTrip.CrewTripType.INCLUSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void 참여중인_크루_여행이_없으면_크루원은_탈퇴된다() {
        // given
        Crew crew = createCrew(LEADER_ID);
        CrewMember member = new CrewMember(crew, 홍석_ID, CrewMemberRole.PARTICIPANT);
        CrewMembers crewMembers = new CrewMembers(crew, LEADER_ID);

        List<CrewMember> crewMemberList = new ArrayList<>(5);
        crewMemberList.add(member);
        crewMemberList.add(new CrewMember(crew, 정수_ID, CrewMemberRole.PARTICIPANT));
        crewMemberList.add(new CrewMember(crew, 준호_ID, CrewMemberRole.PARTICIPANT));
        crewMemberList.add(new CrewMember(crew, 지수_ID, CrewMemberRole.PARTICIPANT));
        crewMemberList.add(new CrewMember(crew, 혁진_ID, CrewMemberRole.PARTICIPANT));

        ReflectionTestUtils.setField(crewMembers, "values", crewMemberList);
        ReflectionTestUtils.setField(crew, "crewMembers", crewMembers);

        List<CrewTrip> crewTrips = Collections.emptyList();

        // when
        crewMembers.withdraw(홍석_ID, crewTrips);

        // then
        assertThat(crewMembers.getValues().contains(member)).isFalse();
    }

    @Test
    void 크루_리더가_탈퇴를_요청하면_예외가_발생한다() {
        // given
        Crew crew = createCrewWithMembers(LEADER_ID);
        List<CrewTrip> crewTrips = List.of(new CrewTrip(TRIP_ID, EXCLUSION));

        // when, then
        assertThatThrownBy(() -> crew.getCrewMembers().withdraw(LEADER_ID, crewTrips))
                .isExactlyInstanceOf(ImpossibleWithdrawCrewException.class);
    }

    @Test
    void 크루_여행_타입이_크루원_전용인_여행에_참여중이면_예외가_발생한다() {
        // given
        Crew crew = createCrewWithMembers(LEADER_ID);
        List<CrewTrip> crewTrips = List.of(new CrewTrip(TRIP_ID, EXCLUSION));

        // when, then
        assertThatThrownBy(() -> crew.getCrewMembers().withdraw(홍석_ID, crewTrips))
                .isExactlyInstanceOf(ImpossibleWithdrawCrewException.class);
    }

    @Test
    void 크루_여행_타입이_크루원_포함인_여행에_참여중이면_크루원이_탈퇴된다() {
        // given
        Crew crew = createCrew(LEADER_ID);
        CrewMember member = new CrewMember(crew, 홍석_ID, CrewMemberRole.PARTICIPANT);
        CrewMembers crewMembers = new CrewMembers(crew, LEADER_ID);

        List<CrewMember> crewMemberList = new ArrayList<>(5);
        crewMemberList.add(member);
        crewMemberList.add(new CrewMember(crew, 정수_ID, CrewMemberRole.PARTICIPANT));
        crewMemberList.add(new CrewMember(crew, 준호_ID, CrewMemberRole.PARTICIPANT));
        crewMemberList.add(new CrewMember(crew, 지수_ID, CrewMemberRole.PARTICIPANT));
        crewMemberList.add(new CrewMember(crew, 혁진_ID, CrewMemberRole.PARTICIPANT));

        ReflectionTestUtils.setField(crewMembers, "values", crewMemberList);
        ReflectionTestUtils.setField(crew, "crewMembers", crewMembers);

        List<CrewTrip> crewTrips = List.of(new CrewTrip(TRIP_ID, INCLUSION));

        // when
        crewMembers.withdraw(홍석_ID, crewTrips);

        // then
        assertThat(crewMembers.getValues().contains(member)).isFalse();
    }

    @Test
    void 크루_리더를_위임한다() {
        // given
        Crew crew = createCrewWithMembers(LEADER_ID);
        CrewMember leader = crew.getCrewMembers().getLeader();

        // when
        CrewMember newLeader = crew.getCrewMembers().delegateLeader(LEADER_ID, 홍석_ID);

        // then
        assertThat(newLeader.getMemberId()).isEqualTo(홍석_ID);
        assertThat(newLeader.getCrewMemberRole()).isEqualTo(CrewMemberRole.LEADER);
        assertThat(leader.getCrewMemberRole()).isEqualTo(CrewMemberRole.PARTICIPANT);
    }
}
