package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CreateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.request.UpdateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.request.crew.CrewOrder;
import com.retrip.crew.application.in.request.demand.CreateDemandRequest;
import com.retrip.crew.application.in.request.demand.DemandOrder;
import com.retrip.crew.application.in.request.demand.UpdateDemandRequest;
import com.retrip.crew.application.in.response.CreateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.RecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.UpdateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.demand.CreateDemandResponse;
import com.retrip.crew.application.in.response.demand.CrewsOfDemandResponse;
import com.retrip.crew.application.in.response.demand.DemandsResponse;
import com.retrip.crew.application.in.response.demand.MyDemandResponse;
import com.retrip.crew.common.ServiceTest;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.entity.RecruitmentQuestion;
import com.retrip.crew.domain.exception.DuplicateDemandException;
import com.retrip.crew.domain.exception.IllegalDemandStateException; // 수정된 import
import com.retrip.crew.domain.exception.common.EntityNotFoundException;
import com.retrip.crew.domain.exception.common.InvalidValueException;
import com.retrip.crew.domain.vo.DemandStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.retrip.crew.common.fixture.CrewFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class DemandServiceTest extends ServiceTest {

    private List<CreateDemandRequest.AnswerRequest> createValidAnswersForCrew(Crew crew, String content) {
        return crew.getRecruitment().getRecruitmentQuestions().getValues().stream()
                .map(q -> new CreateDemandRequest.AnswerRequest(q.getId(), content))
                .collect(Collectors.toList());
    }

    @Test
    void 크루_참여_요청을_생성한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        List<CreateDemandRequest.AnswerRequest> answerRequests = createValidAnswersForCrew(crew, "열정적으로 참여하겠습니다!");
        CreateDemandRequest request = new CreateDemandRequest(MEMBER_ID, answerRequests);

        // when
        CreateDemandResponse response = demandService.createDemand(crew.getId(), request);

        // then
        List<Demand> demands = crew.getRecruitment().getDemands();
        assertAll(
                () -> assertThat(demands).hasSize(1),
                () -> assertThat(response.memberId()).isEqualTo(demands.get(0).getMemberId()),
                () -> assertThat(demands.get(0).getAnswers()).hasSize(answerRequests.size())
        );
    }

    @Test
    void 이미_요청한_사용자는_다시_크루에_요청할_수_없다() {
        // given
        Crew crew = createCrew(LEADER_ID);
        crew.demand(MEMBER_ID);
        Crew savedCrew = crewRepository.save(crew);

        List<CreateDemandRequest.AnswerRequest> answerRequests = createValidAnswersForCrew(savedCrew, "다시 지원합니다!");
        CreateDemandRequest request = new CreateDemandRequest(MEMBER_ID, answerRequests);

        // when, then
        assertThatThrownBy(() -> demandService.createDemand(savedCrew.getId(), request))
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

    @Test
    void 답변_개수가_질문_개수와_다르면_예외가_발생한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        CreateDemandRequest request = new CreateDemandRequest(MEMBER_ID, Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> demandService.createDemand(crew.getId(), request))
                .isInstanceOf(InvalidValueException.class)
                .hasMessageContaining("개수가 일치하지 않습니다.");
    }

    @Test
    void 답변_내용이_너무_길면_예외가_발생한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        String longAnswer = "a".repeat(501);
        List<CreateDemandRequest.AnswerRequest> answerRequests = createValidAnswersForCrew(crew, longAnswer);
        CreateDemandRequest request = new CreateDemandRequest(MEMBER_ID, answerRequests);

        // when & then
        assertThatThrownBy(() -> demandService.createDemand(crew.getId(), request))
                .isInstanceOf(InvalidValueException.class)
                .hasMessageContaining("이하로 입력해야 합니다.");
    }

    @Test
    void 다른_크루의_질문으로_답변하면_예외가_발생한다() {
        // given
        Crew crewA = crewRepository.save(createCrew(LEADER_ID));
        Crew crewB = crewRepository.save(createCrew(UUID.randomUUID()));

        RecruitmentQuestion questionFromCrewB = crewB.getRecruitment().getRecruitmentQuestions().getValues().get(0);
        CreateDemandRequest.AnswerRequest invalidAnswer = new CreateDemandRequest.AnswerRequest(questionFromCrewB.getId(), "잘못된 답변");

        List<CreateDemandRequest.AnswerRequest> answerRequests = crewA.getRecruitment().getRecruitmentQuestions().getValues().stream()
                .skip(1)
                .map(q -> new CreateDemandRequest.AnswerRequest(q.getId(), "정상 답변"))
                .collect(Collectors.toList());
        answerRequests.add(invalidAnswer);

        CreateDemandRequest request = new CreateDemandRequest(MEMBER_ID, answerRequests);

        // when & then
        assertThatThrownBy(() -> demandService.createDemand(crewA.getId(), request))
                .isInstanceOf(InvalidValueException.class)
                .hasMessageContaining("해당 크루의 질문이 아닙니다.");
    }

    @Test
    void 내_크루_참여_요청을_조회한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        List<CreateDemandRequest.AnswerRequest> answerRequests = createValidAnswersForCrew(crew, "제 답변입니다.");
        demandService.createDemand(crew.getId(), new CreateDemandRequest(MEMBER_ID, answerRequests));

        // when
        MyDemandResponse response = demandService.getMyDemand(crew.getId(), MEMBER_ID);

        // then
        assertAll(
                () -> assertThat(response.crewId()).isEqualTo(crew.getId()),
                () -> assertThat(response.status()).isEqualTo(DemandStatus.PENDING),
                () -> assertThat(response.answers()).hasSize(5),
                () -> assertThat(response.answers().get(0).answerContent()).isEqualTo("제 답변입니다.")
        );
    }

    @Test
    void 존재하지_않는_내_참여_요청을_조회하면_예외가_발생한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));

        // when & then
        assertThatThrownBy(() -> demandService.getMyDemand(crew.getId(), MEMBER_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("사용자의 참여 요청을 찾을 수 없습니다.");
    }

    @Test
    void 내_크루_참여_요청을_수정한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        List<CreateDemandRequest.AnswerRequest> initialAnswers = createValidAnswersForCrew(crew, "처음 쓴 답변");
        demandService.createDemand(crew.getId(), new CreateDemandRequest(MEMBER_ID, initialAnswers));

        List<UpdateDemandRequest.AnswerRequest> updatedAnswers = crew.getRecruitment().getRecruitmentQuestions().getValues().stream()
                .map(q -> new UpdateDemandRequest.AnswerRequest(q.getId(), "새롭게 수정한 답변입니다."))
                .toList();
        UpdateDemandRequest updateRequest = new UpdateDemandRequest(MEMBER_ID, updatedAnswers);

        // when
        demandService.updateDemand(crew.getId(), updateRequest);
        MyDemandResponse result = demandService.getMyDemand(crew.getId(), MEMBER_ID);

        // then
        assertThat(result.answers().get(0).answerContent()).isEqualTo("새롭게 수정한 답변입니다.");
    }

    @Test
    void 대기상태가_아닌_참여_요청을_수정하려하면_예외가_발생한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        List<CreateDemandRequest.AnswerRequest> answerRequests = createValidAnswersForCrew(crew, "답변");
        CreateDemandResponse demandResponse = demandService.createDemand(crew.getId(), new CreateDemandRequest(MEMBER_ID, answerRequests));

        demandService.approveDemand(crew.getId(), demandResponse.demandId(), LEADER_ID);

        List<UpdateDemandRequest.AnswerRequest> updatedAnswers = createValidAnswersForCrew(crew, "수정 답변").stream()
                .map(a -> new UpdateDemandRequest.AnswerRequest(a.questionId(), a.content()))
                .toList();
        UpdateDemandRequest updateRequest = new UpdateDemandRequest(MEMBER_ID, updatedAnswers);

        // when & then
        assertThatThrownBy(() -> demandService.updateDemand(crew.getId(), updateRequest))
                .isExactlyInstanceOf(IllegalDemandStateException.class)
                .hasMessageContaining("대기중인 참여 요청만 수정할 수 있습니다.");
    }

    @Test
    void 다른사람의_참여_요청을_수정하려하면_예외가_발생한다() {
        // given
        Crew crew = crewRepository.save(createCrew(LEADER_ID));
        List<CreateDemandRequest.AnswerRequest> answerRequests = createValidAnswersForCrew(crew, "원래 답변");
        demandService.createDemand(crew.getId(), new CreateDemandRequest(MEMBER_ID, answerRequests));

        UUID anotherMemberId = UUID.randomUUID();
        List<UpdateDemandRequest.AnswerRequest> updatedAnswers = createValidAnswersForCrew(crew, "수정 답변").stream()
                .map(a -> new UpdateDemandRequest.AnswerRequest(a.questionId(), a.content()))
                .toList();
        UpdateDemandRequest updateRequest = new UpdateDemandRequest(anotherMemberId, updatedAnswers);

        // when & then
        assertThatThrownBy(() -> demandService.updateDemand(crew.getId(), updateRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("사용자의 참여 요청을 찾을 수 없습니다.");
    }
}