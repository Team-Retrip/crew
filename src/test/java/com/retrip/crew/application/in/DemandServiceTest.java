package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CreateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.request.UpdateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.request.crew.CrewOrder;
import com.retrip.crew.application.in.request.demand.CreateDemandRequest;
import com.retrip.crew.application.in.request.demand.DemandOrder;
import com.retrip.crew.application.in.response.CreateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.RecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.UpdateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.demand.CreateDemandResponse;
import com.retrip.crew.application.in.response.demand.CrewsOfDemandResponse;
import com.retrip.crew.application.in.response.demand.DemandsResponse;
import com.retrip.crew.common.ServiceTest;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.entity.RecruitmentQuestion;
import com.retrip.crew.domain.exception.DuplicateDemandException;
import com.retrip.crew.domain.exception.QuestionNotFoundException;
import com.retrip.crew.domain.vo.DemandStatus;
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
import static com.retrip.crew.common.fixture.CrewFixture.LEADER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class DemandServiceTest extends ServiceTest {
    @Test
    void 크루_참여_요청을_생성한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        CreateDemandRequest request = new CreateDemandRequest(MEMBER_ID);

        // when
        CreateDemandResponse response = demandService.createDemand(crew.getId(), request);

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
        assertThatThrownBy(() -> demandService.createDemand(save.getId(), request))
                .isExactlyInstanceOf(DuplicateDemandException.class);
    }

    @Test
    void 크루_참여_요청을_취소한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        Demand demand = crew.demand(MEMBER_ID);

        // when
        demandService.cancelDemand(crew.getId(), demand.getId(), MEMBER_ID);

        // then
        assertThat(demand.getStatus()).isEqualTo(DemandStatus.CANCELED);
    }

    @ParameterizedTest
    @CsvSource({"PENDING,2", "APPROVED,2", "REJECTED,1"})
    void 리더가_크루_참여_요청_목록을_조회한다(String status, int expected) {
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
                demandService.getDemands(crew.getId(), LEADER_ID, status, pageable, DemandOrder.DATE, "desc");

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
                demandService.getCrewsOfDemand(crew.getId(), demand.getId(), LEADER_ID, pageable, CrewOrder.DATE, "desc");

        // then
        assertThat(response.getTotalElements()).isEqualTo(5);
    }

    @Test
    void 크루_참여_요청_질문을_등록한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        CreateRecruitmentQuestionRequest request = new CreateRecruitmentQuestionRequest("크루 참여 동기를 작성해주세요.");

        // when
        CreateRecruitmentQuestionResponse response =
                demandService.createRecruitmentQuestion(LEADER_ID, crew.getId(), request);

        // then

        // then
        List<RecruitmentQuestion> questions = crew.getRecruitment().getRecruitmentQuestions().getValues();
        assertAll(
                () -> assertThat(questions).hasSize(6),
                () -> assertThat(questions.get(5).getContent().getValue()).isEqualTo(request.content()),
                () -> assertThat(response.content()).isEqualTo(request.content())
        );
    }

    @Test
    void 크루_참여_요청_질문을_수정한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        RecruitmentQuestion question = RecruitmentQuestion.create("크루 참여 동기를 작성해주세요.", crew);
        crew.addRecruitmentQuestion(LEADER_ID, question);

        UpdateRecruitmentQuestionRequest request = new UpdateRecruitmentQuestionRequest("크루 참여 동기를 작성해주세요요요요요");

        // when
        UpdateRecruitmentQuestionResponse response =
                demandService.updateRecruitmentQuestion(LEADER_ID, crew.getId(), question.getId(), request);

        // then
        assertThat(question.getContent().getValue()).isEqualTo(request.content());
        assertThat(response.content()).isEqualTo(request.content());
    }

    @Test
    void 크루_참여_요청_질문을_삭제한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        RecruitmentQuestion question = RecruitmentQuestion.create("크루 참여 동기를 작성해주세요.", crew);
        crew.addRecruitmentQuestion(LEADER_ID, question);

        // when
        demandService.deleteRecruitmentQuestion(LEADER_ID, crew.getId(), question.getId());

        // then
        assertThat(crew.getRecruitment().getRecruitmentQuestions().getValues().size()).isEqualTo(5);
    }

    @Test
    void 크루_참여_요청_질문을_조회한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        RecruitmentQuestion question1 = RecruitmentQuestion.create("크루 참여 동기를 작성해주세요.1", crew);
        RecruitmentQuestion question2 = RecruitmentQuestion.create("크루 참여 동기를 작성해주세요.2", crew);
        crew.addRecruitmentQuestion(LEADER_ID, question1);
        crew.addRecruitmentQuestion(LEADER_ID, question2);

        // when
        List<RecruitmentQuestionResponse> responses =
                demandService.getRecruitmentQuestions(LEADER_ID, crew.getId());

        // then
        assertThat(responses).hasSize(7);
        assertThat(responses)
                .extracting(RecruitmentQuestionResponse::content)
                .containsExactlyInAnyOrder(
                        "크루 참여 동기를 작성해주세요.1",
                        "크루 참여 동기를 작성해주세요.2",
                        "크루에 지원한 이유는 무엇인가요?",
                        "어떤 활동을 기대하고 있나요?",
                        "자신을 한 문장으로 표현한다면?",
                        "크루에서 어떤 역할을 하고 싶나요?",
                        "추가로 하고 싶은 말이 있나요?"
                );
    }
}
