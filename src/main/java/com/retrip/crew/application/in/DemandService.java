package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CreateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.request.UpdateRecruitmentQuestionRequest;
import com.retrip.crew.application.in.request.crew.CrewOrder;
import com.retrip.crew.application.in.request.demand.CreateDemandRequest;
import com.retrip.crew.application.in.request.demand.DemandOrder;
import com.retrip.crew.application.in.response.CreateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.RecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.UpdateRecruitmentQuestionResponse;
import com.retrip.crew.application.in.response.demand.ApproveDemandResponse;
import com.retrip.crew.application.in.response.demand.ChangeRecruitmentStatusResponse;
import com.retrip.crew.application.in.response.demand.CreateDemandResponse;
import com.retrip.crew.application.in.response.demand.CrewsOfDemandResponse;
import com.retrip.crew.application.in.response.demand.DemandsResponse;
import com.retrip.crew.application.in.response.demand.RejectDemandResponse;
import com.retrip.crew.application.in.usecase.ManageDemandUseCase;
import com.retrip.crew.application.in.usecase.ManageRecruitmentQuestionUseCase;
import com.retrip.crew.application.in.usecase.UpdateRecruitmentUseCase;
import com.retrip.crew.application.out.repository.CrewDemandRepository;
import com.retrip.crew.application.out.repository.CrewQueryRepository;
import com.retrip.crew.application.out.repository.CrewRepository;
import com.retrip.crew.application.out.repository.RecruitmentQuestionQueryRepository;
import com.retrip.crew.application.out.repository.RecruitmentQuestionRepository;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.entity.RecruitmentQuestion;
import com.retrip.crew.domain.exception.CrewNotFoundException;
import com.retrip.crew.domain.exception.NotCrewLeaderException;
import com.retrip.crew.domain.exception.QuestionNotFoundException;
import com.retrip.crew.domain.exception.common.BusinessException;
import com.retrip.crew.domain.exception.common.EntityNotFoundException;
import com.retrip.crew.domain.vo.DemandStatus;
import com.retrip.crew.infra.util.PaginationUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DemandService implements UpdateRecruitmentUseCase, ManageDemandUseCase, ManageRecruitmentQuestionUseCase {
    private final CrewRepository crewRepository;
    private final CrewDemandRepository demandRepository;
    private final CrewQueryRepository crewQueryRepository;
    private final RecruitmentQuestionRepository recruitmentQuestionRepository;
    private final RecruitmentQuestionQueryRepository recruitmentQuestionQueryRepository;

    @Override
    public ChangeRecruitmentStatusResponse startRecruitment(UUID crewId) {
        Crew crew = findById(crewId);
        crew.startRecruitment();
        return ChangeRecruitmentStatusResponse.of(crew);
    }

    @Override
    public ChangeRecruitmentStatusResponse stopRecruitment(UUID crewId) {
        Crew crew = findById(crewId);
        crew.stopRecruitment();
        return ChangeRecruitmentStatusResponse.of(crew);
    }

    @Override
    public CreateDemandResponse createDemand(UUID crewId, CreateDemandRequest request) {
        Crew crew = findById(crewId);
        Demand demand = crew.demand(request.memberId());
        return CreateDemandResponse.of(crew.getId(), demand);
    }

    @Override
    public Page<DemandsResponse> getDemands(
            UUID crewId, UUID memberId, String status, Pageable pageable, DemandOrder order, String sort) {
        Crew crew = findById(crewId);
        throwIfNotLeader(crew, memberId, new NotCrewLeaderException());
        Page<Demand> demands = demandRepository.findByCrewIdAndStatus(
                crewId, DemandStatus.valueOf(status), PaginationUtils.createPageRequest(pageable, order.getField(), sort));
        return demands.map(d -> DemandsResponse.of(crewId, d));
    }

    @Override
    public Page<CrewsOfDemandResponse> getCrewsOfDemand(
            UUID crewId, UUID demandId, UUID memberId, Pageable pageable, CrewOrder order, String sort) {
        Crew crew = findById(crewId);
        throwIfNotLeader(crew, memberId, new NotCrewLeaderException());
        Demand demand = findDemandByIdAndCrewId(demandId, crewId);
        return crewQueryRepository.findAllContainsMember(pageable, demand.getMemberId());
    }

    private static void throwIfNotLeader(Crew crew, UUID memberId, BusinessException exception) {
        if (!crew.getCrewMembers().isLeader(memberId)) {
            throw exception;
        }
    }

    @Override
    public void cancelDemand(UUID crewId, UUID demandId, UUID memberId) {
        Demand demand = findDemandByIdAndCrewId(demandId, crewId);
        Crew crew = demand.getCrew();
        crew.cancelDemand(demand);
    }

    @Override
    public RejectDemandResponse rejectDemand(UUID crewId, UUID demandId, UUID memberId) {
        Demand demand = findDemandByIdAndCrewId(demandId, crewId);
        Crew crew = demand.getCrew();
        crew.rejectDemand(demand);
        return RejectDemandResponse.of(demand);
    }

    @Override
    public ApproveDemandResponse approveDemand(UUID crewId, UUID demandId, UUID memberId) {
        Demand demand = findDemandByIdAndCrewId(demandId, crewId);
        Crew crew = demand.getCrew();
        crew.approveDemand(demand);
        return ApproveDemandResponse.of(demand);
    }

    private Demand findDemandByIdAndCrewId(UUID demandId, UUID crewId) {
        return demandRepository.findCrewByIdAndCrewId(demandId, crewId)
                .orElseThrow(() -> new EntityNotFoundException("참여 요청 엔티티를 찾을 수 없습니다."));
    }

    private Crew findById(UUID crewId){
        return crewRepository.findById(crewId)
                .orElseThrow(CrewNotFoundException::new);
    }

    @Override
    public CreateRecruitmentQuestionResponse createRecruitmentQuestion(UUID memberId, UUID crewId, CreateRecruitmentQuestionRequest request) {
        Crew crew = findById(crewId);
        RecruitmentQuestion question = RecruitmentQuestion.create(request.content(), crew);
        crew.addRecruitmentQuestion(memberId, question);

        return CreateRecruitmentQuestionResponse.of(question);
    }

    @Override
    public UpdateRecruitmentQuestionResponse updateRecruitmentQuestion(UUID memberId, UUID crewId, UUID questionId, UpdateRecruitmentQuestionRequest request) {
        RecruitmentQuestion savedQuestion = findRecruitmentQuestionByIdAndCrewId(questionId, crewId);
        savedQuestion.update(request.content(), memberId);
        return UpdateRecruitmentQuestionResponse.of(savedQuestion);
    }

    @Override
    public void deleteRecruitmentQuestion(UUID memberId, UUID crewId, UUID questionId) {
        Crew crew = findById(crewId);
        RecruitmentQuestion savedQuestion = findRecruitmentQuestionByIdAndCrewId(questionId, crewId);
        crew.deleteRecruitmentQuestion(memberId, savedQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruitmentQuestionResponse> getRecruitmentQuestions(UUID memberId, UUID crewId) {
        return recruitmentQuestionQueryRepository.findRecruitmentQuestions(crewId);
    }

    private RecruitmentQuestion findRecruitmentQuestionByIdAndCrewId(UUID questionId, UUID crewId) {
        return recruitmentQuestionRepository.findByIdAndCrewId(questionId, crewId)
                .orElseThrow(QuestionNotFoundException::new);
    }
}
