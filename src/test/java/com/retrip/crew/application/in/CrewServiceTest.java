package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.IntroductionCreateRequest;
import com.retrip.crew.application.in.request.IntroductionDeleteRequest;
import com.retrip.crew.application.in.request.IntroductionUpdateRequest;
import com.retrip.crew.application.in.request.crew.CrewCreateRequest;
import com.retrip.crew.application.in.request.crew.CrewOrder;
import com.retrip.crew.application.in.request.crew.CrewUpdateRequest;
import com.retrip.crew.application.in.request.demand.CreateDemandRequest;
import com.retrip.crew.application.in.response.IntroductionCreateResponse;
import com.retrip.crew.application.in.response.IntroductionDetailResponse;
import com.retrip.crew.application.in.response.crew.CrewCreateResponse;
import com.retrip.crew.application.in.response.crew.CrewDetailResponse;
import com.retrip.crew.application.in.response.crew.CrewListResponse;
import com.retrip.crew.application.in.response.crew.CrewUpdateResponse;
import com.retrip.crew.application.in.response.demand.CreateDemandResponse;
import com.retrip.crew.common.ServiceTest;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.CrewMemberRole;
import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.entity.Introduction;
import com.retrip.crew.domain.exception.common.InvalidAccessException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

class CrewServiceTest extends ServiceTest {
    @Test
    void 크루를_생성_한다() {
        //given
        CrewCreateRequest request = createCrewRequest(
                LEADER_ID,
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                createDefaultQuestions()

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
        Crew crew = crewRepository.save(Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID,
                createDefaultQuestions()
        ));
        CreateDemandRequest request = new CreateDemandRequest(MEMBER_ID);

        CreateDemandResponse response = demandService.createDemand(crew.getId(), request);

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
                MEMBER_ID,
                List.of("질문1")
        );
        crew.demand(MEMBER_ID);
        crew.demand(UUID.randomUUID());
        crew.demand(UUID.randomUUID());
        Crew save = crewRepository.save(crew);
        CreateDemandRequest request = new CreateDemandRequest(MEMBER_ID);

        assertThatThrownBy(() -> demandService.createDemand(save.getId(), request))
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
                5,
                List.of("질문1")
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
                5,
                List.of("질문1")
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
                MEMBER_ID,
                createDefaultQuestions()
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
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID,
                createDefaultQuestions()
        );
        Introduction introduction = Introduction.create(
                MEMBER_ID,
                "정수의 자기소개!",
                "안녕하세요!",
                crew
        );
        crew.addIntroduction(introduction);
        Crew savedCrew = crewRepository.save(crew);

        IntroductionUpdateRequest request = new IntroductionUpdateRequest(MEMBER_ID, "변경된 자기소개!", "안녕!");

        // when
        crewService.updateIntroduction(savedCrew.getId(), introduction.getId(), request);
        Introduction result = crewService.findIntroductionByIdAndCrewId(introduction.getId(), savedCrew.getId());

        // then
        assertThat(result.getTitle()).isEqualTo("변경된 자기소개!");
        assertThat(result.getContent()).isEqualTo("안녕!");
    }

    @Test
    void 일반_멤버는_다른_멤버의_자기소개를_수정할_수_없다() {
        // given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID,
                createDefaultQuestions()
        );
        Introduction introduction = Introduction.create(
                MEMBER_ID,
                "정수의 자기소개!",
                "안녕하세요!",
                crew
        );
        crew.addIntroduction(introduction);
        Crew savedCrew = crewRepository.save(crew);

        UUID otherMemberId = UUID.randomUUID();
        IntroductionUpdateRequest request = new IntroductionUpdateRequest(otherMemberId, "수정된 소개", "안");

        // when & then
        assertThrows(InvalidAccessException.class, () -> crewService.updateIntroduction(savedCrew.getId(), introduction.getId(), request));
    }

    @Test
    void 리더가_자기소개를_삭제할_수_있다() {
        // given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID,
                createDefaultQuestions()
        );
        Introduction introduction = Introduction.create(
                MEMBER_ID,
                "정수의 자기소개!",
                "안녕하세요!",
                crew
        );
        crew.addIntroduction(introduction);
        Crew savedCrew = crewRepository.save(crew);

        IntroductionDeleteRequest request = new IntroductionDeleteRequest(MEMBER_ID);

        // when
        crewService.deleteIntroduction(savedCrew.getId(), introduction.getId(), request);

        // then
        assertThat(savedCrew.getIntroductions().getValues().isEmpty()).isTrue();
    }

    @Test
    void 자기소개를_조회한다() {
        // given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID,
                createDefaultQuestions()
        );
        Introduction introduction = Introduction.create(
                MEMBER_ID,
                "정수의 자기소개!",
                "안녕하세요!",
                crew
        );
        crew.addIntroduction(introduction);
        Crew savedCrew = crewRepository.save(crew);

        // when
        IntroductionDetailResponse response = crewService.getIntroduction(savedCrew.getId(), introduction.getId());

        // then
        assertThat(response.introductionId()).isEqualTo(introduction.getId());
        assertThat(response.content()).isEqualTo("안녕하세요!");
        assertThat(response.title()).isEqualTo("정수의 자기소개!");
    }
}
