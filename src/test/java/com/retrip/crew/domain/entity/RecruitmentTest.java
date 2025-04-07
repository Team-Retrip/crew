package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.IllegalDemandStateException;
import com.retrip.crew.domain.vo.DemandStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.retrip.crew.common.fixture.CrewFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class RecruitmentTest {
    @Test
    void 모집을_생성한다() {
        assertThatCode(() -> new Recruitment(100))
                .doesNotThrowAnyException();
    }

    @Test
    void 참여_요청을_취소한다() {
        // given
        Crew crew = createCrew(LEADER_ID);
        Recruitment recruitment = new Recruitment(100);
        Demand demand = new Demand(정수_ID, crew);
        List<Demand> demands = List.of(
                demand,
                new Demand(홍석_ID, crew),
                new Demand(준호_ID, crew),
                new Demand(지수_ID, crew),
                new Demand(혁진_ID, crew)
        );
        ReflectionTestUtils.setField(recruitment, "demands", demands);

        // when
        recruitment.cancelDemand(demand);

        // then
        assertThat(demand.getStatus()).isEqualTo(DemandStatus.CANCELED);
    }

    @Test
    void 취소한_참여_요청을_재요청_한다() {
        // given
        Crew crew = createCrew(LEADER_ID);
        Recruitment recruitment = new Recruitment(100);
        Demand demand = new Demand(정수_ID, crew);
        List<Demand> demands = List.of(
                demand,
                new Demand(홍석_ID, crew),
                new Demand(준호_ID, crew),
                new Demand(지수_ID, crew),
                new Demand(혁진_ID, crew)
        );
        ReflectionTestUtils.setField(demand, "status", DemandStatus.CANCELED);
        ReflectionTestUtils.setField(recruitment, "demands", demands);

        // when
        recruitment.addDemand(정수_ID, crew);

        // then
        assertThat(demand.getStatus()).isEqualTo(DemandStatus.PENDING);
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = {"PENDING"})
    void 대기_상태가_아니면_참여_요청을_취소할_수_있다(DemandStatus status) {
        // given
        Crew crew = createCrew(LEADER_ID);
        Recruitment recruitment = new Recruitment(100);
        Demand demand = new Demand(정수_ID, crew);
        List<Demand> demands = List.of(
                demand,
                new Demand(홍석_ID, crew),
                new Demand(준호_ID, crew),
                new Demand(지수_ID, crew),
                new Demand(혁진_ID, crew)
        );
        ReflectionTestUtils.setField(demand, "status", status);
        ReflectionTestUtils.setField(recruitment, "demands", demands);

        // when, then
        assertThatThrownBy(() -> recruitment.cancelDemand(demand))
                .isExactlyInstanceOf(IllegalDemandStateException.class);
    }

    @Test
    void 참여_요청을_승인한다() {
        // given
        Crew crew = createCrew(LEADER_ID);
        Recruitment recruitment = new Recruitment(100);
        Demand demand = new Demand(정수_ID, crew);
        ReflectionTestUtils.setField(recruitment, "demands", List.of(demand));

        // when
        recruitment.approveDemand(demand);

        // then
        assertThat(demand.getStatus()).isEqualTo(DemandStatus.APPROVED);
    }
}
