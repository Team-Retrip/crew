package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.request.CrewUpdateRequest;
import com.retrip.crew.application.in.response.CrewCreateResponse;
import com.retrip.crew.application.in.response.CrewUpdateResponse;
import com.retrip.crew.application.out.repository.CrewRepository;
import com.retrip.crew.domain.entity.Crew;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CrewServiceTest extends BaseTest {
    @Autowired
    CrewRepository crewRepository;

    CrewService crewService;
    UUID memberId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        crewService = new CrewService(crewRepository);
    }

    @Test
    void 크루를_생성_한다() {
        //given
        CrewCreateRequest request = createCrew(
                정수_ID,
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
        Crew crew = crewRepository.save(Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                memberId
        ));
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
        Crew crew = crewRepository.save(Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                memberId
        ));
        CreateDemandRequest request = new CreateDemandRequest(memberId);

        CreateDemandResponse response = crewService.createDemand(crew.getId(), request);

        List<Demand> demands = crew.getRecruitment().getDemands();
        assertAll(
                () -> assertThat(demands.size()).isEqualTo(1),
                () -> assertThat(response.memberId()).isEqualTo(demands.get(0).getMemberId())
        );
    }

    @Test
    void 이미_요청한_사용자는_다시_크루에_요청할_수_없다() {
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                memberId
        );
        crew.demand(memberId);
        crew.demand(UUID.randomUUID());
        crew.demand(UUID.randomUUID());
        Crew save = crewRepository.save(crew);
        CreateDemandRequest request = new CreateDemandRequest(memberId);

        assertThatThrownBy(() -> crewService.createDemand(save.getId(), request))
                .isExactlyInstanceOf(IllegalStateException.class);
    }

    @Test
    void 크루를_검색_및_정렬_필터링하여_조회한다(){
        //given
        List<CrewCreateRequest> requests = createMultipleCrews(
                10,
                정수_ID,
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
        CrewCreateRequest request = createCrew(
                정수_ID,
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
                () -> assertThat(response.leaderId()).isEqualTo(정수_ID),
                () -> assertThat(response.members().size()).isEqualTo(1),
                () -> assertThat(response.members().getFirst().roleCode()).isEqualTo(CrewMemberRole.LEADER.getCode()),
                () -> assertThat(response.members().getFirst().memberId()).isEqualTo(정수_ID)
        );
    }
}
