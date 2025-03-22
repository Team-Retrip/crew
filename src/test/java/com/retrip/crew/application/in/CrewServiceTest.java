package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CreateDemandRequest;
import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.request.CrewOrder;
import com.retrip.crew.application.in.request.CrewUpdateRequest;
import com.retrip.crew.application.in.response.*;
import com.retrip.crew.common.ServiceTest;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.CrewMemberRole;
import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.exception.common.IllegalStateException;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static com.retrip.crew.common.fixture.CrewFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class CrewServiceTest extends ServiceTest {
    @Test
    void 크루를_생성_한다() {
        //given
        CrewCreateRequest request = createCrewRequest(
                LEADER_ID,
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100
        );

        //when
        CrewCreateResponse response = crewService.createCrew(request);

        //then
        assertThat(response.id()).isNotNull();
        assertThat(response.leaderId()).isNotNull();
    }

    @Test
    void 크루의_제목_설명_최대_인원수를_수정한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        CrewUpdateRequest request = new CrewUpdateRequest(
                "강릉 크루원 구함",
                "강릉 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                200
        );

        // when
        CrewUpdateResponse response = crewService.updateCrew(crew.getId(), request);

        // then
        assertAll(
                () -> assertThat(response.title()).isEqualTo("강릉 크루원 구함"),
                () -> assertThat(response.maxMembers()).isEqualTo(200)
        );
    }

    @Test
    void 크루_참여_요청을_생성한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        CreateDemandRequest request = new CreateDemandRequest(MEMBER_ID);

        // when
        CreateDemandResponse response = crewService.createDemand(crew.getId(), request);

        // then
        List<Demand> demands = crew.getRecruitment().getDemands();
        assertAll(
                () -> assertThat(demands.size()).isEqualTo(1),
                () -> assertThat(response.memberId()).isEqualTo(demands.get(0).getMemberId())
        );
    }

    @Test
    void 이미_요청한_사용자는_다시_크루에_요청할_수_없다() {
        // given
        Crew crew = createCrew(LEADER_ID);
        crew.demand(MEMBER_ID);
        crew.demand(UUID.randomUUID());
        crew.demand(UUID.randomUUID());
        Crew save = crewRepository.save(crew);
        CreateDemandRequest request = new CreateDemandRequest(MEMBER_ID);

        // when, then
        assertThatThrownBy(() -> crewService.createDemand(save.getId(), request))
                .isExactlyInstanceOf(IllegalStateException.class);
    }

    @Test
    void 크루를_검색_및_정렬_필터링하여_조회한다(){
        //given
        List<CrewCreateRequest> requests = createMultipleCrews(
                10,
                MEMBER_ID,
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                5
        );
        requests.forEach(request -> {
            CrewCreateResponse response = crewService.createCrew(request);
        });
        Pageable pageable = PageRequest.of(0, 5);

        //when
        ScrollPageResponse<CrewListResponse> response = crewService.getCrews(pageable, "속초 크루원", CrewOrder.DATE, "desc");

        //then
        assertAll(
                "크루 검색 및 정렬 검증",
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getList()).hasSize(pageable.getPageSize()),
                () -> assertThat(response.getList().getFirst().title()).isEqualTo("속초 크루원 구함 10"),
                () -> assertThat(response.getList().getFirst().memberCount()).isEqualTo(1),
                () -> assertThat(response.getList().getFirst().maxMemberCount()).isEqualTo(5),
                () -> assertThat(response.isHasNext()).isTrue()
        );
    }

    @Test
    void 크루_상세를_조회한다(){
        //given
        CrewCreateRequest request = createCrewRequest(
                MEMBER_ID,
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                5
        );
        UUID crewId = crewService.createCrew(request).id();

        //when
        CrewDetailResponse response = crewService.getCrewDetail(crewId);

        //then
        assertAll(
                () -> assertThat(response.id()).isEqualTo(crewId),
                () -> assertThat(response.leaderId()).isEqualTo(MEMBER_ID),
                () -> assertThat(response.members().size()).isEqualTo(1),
                () -> assertThat(response.members().getFirst().roleCode()).isEqualTo(CrewMemberRole.LEADER.getCode()),
                () -> assertThat(response.members().getFirst().memberId()).isEqualTo(MEMBER_ID)
        );
    }
}
