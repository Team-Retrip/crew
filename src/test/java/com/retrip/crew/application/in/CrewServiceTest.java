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
import com.retrip.crew.domain.entity.Introduction;
import com.retrip.crew.domain.exception.common.IllegalStateException;
import com.retrip.crew.domain.exception.common.InvalidAccessException;
import com.retrip.crew.domain.exception.DuplicateDemandException;
import com.retrip.crew.domain.vo.DemandStatus;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

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
                .isExactlyInstanceOf(DuplicateDemandException.class);
    }

    @Test
    void 크루_참여_요청을_취소한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        Demand demand = crew.demand(MEMBER_ID);

        // when
        crewService.cancelDemand(crew.getId(), demand.getId(), MEMBER_ID);

        // then
        assertThat(demand.getStatus()).isEqualTo(DemandStatus.CANCELED);
    }

    @ParameterizedTest
    @CsvSource({"PENDING,2", "APPROVED,2", "REJECTED,1"})
    void 리더가_크루_요청_목록을_조회한다(String status, int expected) {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        Demand 정수 = new Demand(정수_ID, crew);
        Demand 홍석 = new Demand(홍석_ID, crew);
        Demand 준호 = new Demand(준호_ID, crew);
        Demand 지수 = new Demand(지수_ID, crew);
        Demand 혁진 = new Demand(혁진_ID, crew);
        List<Demand> demands = List.of(정수, 홍석, 준호, 지수, 혁진);

        ReflectionTestUtils.setField(준호, "status", DemandStatus.APPROVED);
        ReflectionTestUtils.setField(지수, "status", DemandStatus.APPROVED);
        ReflectionTestUtils.setField(혁진, "status", DemandStatus.REJECTED);
        ReflectionTestUtils.setField(crew.getRecruitment(), "demands", demands);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<DemandsResponse> response =
                crewService.getDemands(crew.getId(), LEADER_ID, status, pageable, DemandOrder.DATE, "desc");

        // then
        assertThat(response.getTotalElements()).isEqualTo(expected);
    }

    @Test
    void 참여_요청자가_속한_크루_목록을_조회한다() {
        // given
        Crew crew = createCrew(LEADER_ID);
        Demand demand = new Demand(홍석_ID, crew);
        ReflectionTestUtils.setField(crew.getRecruitment(), "demands", List.of(demand));

        crewRepository.save(crew);
        crewRepository.save(createCrewWithMembers(MEMBER_ID));
        crewRepository.save(createCrewWithMembers(MEMBER_ID));
        crewRepository.save(createCrewWithMembers(MEMBER_ID));
        crewRepository.save(createCrewWithMembers(MEMBER_ID));
        crewRepository.save(createCrewWithMembers(MEMBER_ID));
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<CrewsOfDemandResponse> response =
                crewService.getCrewsOfDemand(crew.getId(), demand.getId(), LEADER_ID, pageable, CrewOrder.DATE, "desc");

        // then
        assertThat(response.getTotalElements()).isEqualTo(5);
    }

    @Test
    void 크루를_검색_및_정렬_필터링하여_조회한다() {
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
    void 크루_상세를_조회한다() {
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

    @Test
    void 자기소개를_등록한다() {
        // given
        Crew crew = crewRepository.save(Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID
        ));
        IntroductionCreateRequest request = new IntroductionCreateRequest(MEMBER_ID, "정수의 자기소개!", "안녕하세요!");

        // when
        IntroductionCreateResponse response = crewService.createIntroduction(crew.getId(), request);

        // then
        assertThat(response.crewId()).isNotNull();
    }

    @Test
    void 내가_작성한_자기소개를_수정할_수_있다() {
        // given
        Crew crew = crewRepository.save(Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID
        ));

        Introduction introduction = introductionRepository.save(Introduction.create(
                MEMBER_ID,
                "정수의 자기소개!",
                "안녕하세요!",
                crew
        ));
        IntroductionUpdateRequest request = new IntroductionUpdateRequest(MEMBER_ID, "변경된 자기소개!", "안녕!");

        // when
        crewService.updateIntroduction(crew.getId(), introduction.getId(), request);
        Introduction result = crewService.findIntroductionByIdAndCrewId(introduction.getId(), crew.getId());

        // then
        assertThat(result.getTitle()).isEqualTo("변경된 자기소개!");
        assertThat(result.getContent()).isEqualTo("안녕!");
    }

    @Test
    void 일반_멤버는_다른_멤버의_자기소개를_수정할_수_없다() {
        // given
        Crew crew = crewRepository.save(Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID
        ));

        Introduction introduction = introductionRepository.save(Introduction.create(
                MEMBER_ID,
                "정수의 자기소개!",
                "안녕하세요!",
                crew
        ));

        UUID otherMemberId = UUID.randomUUID();
        IntroductionUpdateRequest request = new IntroductionUpdateRequest(otherMemberId, "수정된 소개", "안");

        // when & then
        assertThrows(InvalidAccessException.class, () -> crewService.updateIntroduction(crew.getId(), introduction.getId(), request));
    }

    @Test
    void 리더가_자기소개를_삭제할_수_있다() {
        // given
        Crew crew = crewRepository.save(Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID
        ));

        Introduction introduction = introductionRepository.save(Introduction.create(
                MEMBER_ID,
                "정수의 자기소개!",
                "안녕하세요!",
                crew
        ));

        entityManager.flush();
        entityManager.clear();

        IntroductionDeleteRequest request = new IntroductionDeleteRequest(MEMBER_ID);

        // when
        crewService.deleteIntroduction(crew.getId(), introduction.getId(), request);

        entityManager.flush();
        entityManager.clear();

        Optional<Introduction> introductionOptional = introductionRepository.findById(introduction.getId());

        // then
        assertThat(introductionOptional.isEmpty()).isTrue();
    }

    @Test
    void 자기소개를_조회한다() {
        // given
        Crew crew = crewRepository.save(Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID
        ));

        Introduction introduction = introductionRepository.save(Introduction.create(
                MEMBER_ID,
                "정수의 자기소개!",
                "안녕하세요!",
                crew
        ));

        // when
        IntroductionDetailResponse response = crewService.getIntroduction(crew.getId(), introduction.getId());

        // then
        assertThat(response.introductionId()).isEqualTo(introduction.getId());
        assertThat(response.content()).isEqualTo("안녕하세요!");
        assertThat(response.title()).isEqualTo("정수의 자기소개!");
    }
}
