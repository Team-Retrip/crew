package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CreateDemandRequest;
import com.retrip.crew.application.in.request.IntroductionCreateRequest;
import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.request.CrewOrder;
import com.retrip.crew.application.in.request.CrewUpdateRequest;
import com.retrip.crew.application.in.request.IntroductionDeleteRequest;
import com.retrip.crew.application.in.request.IntroductionUpdateRequest;
import com.retrip.crew.application.in.response.*;
import com.retrip.crew.application.in.request.crew.CrewCreateRequest;
import com.retrip.crew.application.in.request.crew.CrewOrder;
import com.retrip.crew.application.in.request.crew.CrewUpdateRequest;
import com.retrip.crew.application.in.request.demand.CreateDemandRequest;
import com.retrip.crew.application.in.request.demand.DemandOrder;
import com.retrip.crew.application.in.response.crew.CrewCreateResponse;
import com.retrip.crew.application.in.response.crew.CrewDetailResponse;
import com.retrip.crew.application.in.response.crew.CrewListResponse;
import com.retrip.crew.application.in.response.crew.CrewUpdateResponse;
import com.retrip.crew.application.in.response.demand.*;
import com.retrip.crew.application.in.usecase.GetCrewUseCase;
import com.retrip.crew.application.in.usecase.ManageCrewUseCase;
import com.retrip.crew.application.in.usecase.ManageDemandUseCase;
import com.retrip.crew.application.in.usecase.ManageIntroductionUseCase;
import com.retrip.crew.application.in.usecase.UpdateRecruitmentUseCase;
import com.retrip.crew.application.out.repository.CrewDemandRepository;
import com.retrip.crew.application.out.repository.CrewMemberRepository;
import com.retrip.crew.application.out.repository.CrewQueryRepository;
import com.retrip.crew.application.out.repository.CrewRepository;
import com.retrip.crew.application.out.repository.IntroductionQueryRepository;
import com.retrip.crew.application.out.repository.IntroductionRepository;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.entity.Introduction;
import com.retrip.crew.domain.entity.Recruitment;
import com.retrip.crew.domain.exception.CrewNotFoundException;
import com.retrip.crew.domain.exception.IntroductionNotFoundException;
import com.retrip.crew.domain.exception.NotCrewLeaderException;
import com.retrip.crew.domain.exception.common.BusinessException;
import com.retrip.crew.domain.exception.common.EntityNotFoundException;
import com.retrip.crew.domain.vo.CrewDescription;
import com.retrip.crew.domain.vo.CrewTitle;
import com.retrip.crew.domain.vo.IntroductionContent;
import com.retrip.crew.domain.vo.IntroductionTitle;
import com.retrip.crew.domain.vo.DemandStatus;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import com.retrip.crew.infra.util.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CrewService implements ManageCrewUseCase, UpdateRecruitmentUseCase, ManageDemandUseCase, GetCrewUseCase, ManageIntroductionUseCase{

    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewQueryRepository crewQueryRepository;
    private final CrewDemandRepository demandRepository;
    private final IntroductionRepository introductionRepository;
    private final IntroductionQueryRepository introductionQueryRepository;

    @Override
    public CrewCreateResponse createCrew(CrewCreateRequest request) {
        Crew crew = crewRepository.save(request.to(request.leader()));
        return CrewCreateResponse.of(crew);
    }

    @Override
    public CrewUpdateResponse updateCrew(UUID crewId, CrewUpdateRequest request) {
        Crew crew = findCrewById(crewId);
        CrewTitle title = new CrewTitle(request.title());
        CrewDescription description = new CrewDescription(request.description());
        Recruitment recruitment = crew.getRecruitment();

        crew.update(title, description);
        recruitment.updateMaxMembers(request.maxMembers());
        return CrewUpdateResponse.of(crew);
    }

    @Override
    public ChangeRecruitmentStatusResponse startRecruitment(UUID crewId) {
        Crew crew = findCrewById(crewId);
        crew.startRecruitment();
        return ChangeRecruitmentStatusResponse.of(crew);
    }

    @Override
    public ChangeRecruitmentStatusResponse stopRecruitment(UUID crewId) {
        Crew crew = findCrewById(crewId);
        crew.stopRecruitment();
        return ChangeRecruitmentStatusResponse.of(crew);
    }

    @Override
    public CreateDemandResponse createDemand(UUID crewId, CreateDemandRequest request) {
        Crew crew = findCrewById(crewId);
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

    @Override
    @Transactional(readOnly = true)
    public ScrollPageResponse<CrewListResponse> getCrews(Pageable pageable, String keyword, CrewOrder order, String sort) {
        Pageable orderPageable = PaginationUtils.createPageRequest(pageable, order.getField(), sort);
        Slice<CrewListResponse> result = crewQueryRepository.getCrews(orderPageable, keyword);
        Long totalCount = crewQueryRepository.getCrewCount(keyword);
        return ScrollPageResponse.of(totalCount, result.hasNext(), result.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public CrewDetailResponse getCrewDetail(UUID crewId) {
        Crew crew = findCrewById(crewId);
        int memberCount = crewMemberRepository.countByCrewId(crewId);
        return CrewDetailResponse.of(crew, memberCount);
    }

    @Override
    public IntroductionCreateResponse createIntroduction(UUID crewId, IntroductionCreateRequest request) {
        Crew crew = findCrewById(crewId);
        Introduction introduction = request.to(crew);
        crew.addIntroduction(introduction);
        return IntroductionCreateResponse.from(introduction);
    }

    @Override
    public IntroductionUpdateResponse updateIntroduction(UUID crewId, UUID introductionId, IntroductionUpdateRequest request) {
        Introduction introduction = findIntroductionByIdAndCrewId(introductionId, crewId);
        introduction.update(
                new IntroductionTitle(request.title()),
                new IntroductionContent(request.content()),
                request.loginMemberId()
        );
        return IntroductionUpdateResponse.from(introduction);
    }

    @Override
    public void deleteIntroduction(UUID crewId, UUID introductionId, IntroductionDeleteRequest request) {
        Crew crew = findCrewById(crewId);
        Introduction introduction = findIntroductionById(introductionId);
        introduction.delete(request.loginMemberId(), crew);
    }

    @Override
    public IntroductionDetailResponse getIntroduction(UUID crewId, UUID introductionId) {
        Introduction introduction = findIntroductionByIdAndCrewId(introductionId, crewId);
        return IntroductionDetailResponse.of(introduction);
    }

    @Override
    public ScrollPageResponse<IntroductionListResponse> getIntroductions(UUID crewId, Pageable pageable) {
        findCrewById(crewId);
        Slice<IntroductionListResponse> result = introductionQueryRepository.getIntroductions(crewId, pageable);
        Long totalCount = introductionQueryRepository.getIntroductionCount(crewId);
        return ScrollPageResponse.of(totalCount, result.hasNext(), result.getContent());
    }

    public Crew findCrewById(UUID crewId){
        return crewRepository.findById(crewId)
                .orElseThrow(CrewNotFoundException::new);
    }

    public Introduction findIntroductionById(UUID introductionId){
        return introductionRepository.findById(introductionId)
                .orElseThrow(IntroductionNotFoundException::new);
    }

    public Introduction findIntroductionByIdAndCrewId(UUID introductionId, UUID crewId) {
        return introductionRepository.findByIdAndCrewId(introductionId, crewId)
                .orElseThrow(IntroductionNotFoundException::new);
    }
}
