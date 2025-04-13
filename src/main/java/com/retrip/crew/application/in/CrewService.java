package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CreateDemandRequest;
import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.request.CrewOrder;
import com.retrip.crew.application.in.request.CrewUpdateRequest;
import com.retrip.crew.application.in.response.*;
import com.retrip.crew.application.in.usecase.GetCrewUseCase;
import com.retrip.crew.application.in.usecase.ManageCrewUseCase;
import com.retrip.crew.application.in.usecase.ManageDemandUseCase;
import com.retrip.crew.application.in.usecase.UpdateRecruitmentUseCase;
import com.retrip.crew.application.out.repository.CrewMemberRepository;
import com.retrip.crew.application.out.repository.CrewQueryRepository;
import com.retrip.crew.application.out.repository.CrewRepository;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Demand;
import com.retrip.crew.domain.entity.Recruitment;
import com.retrip.crew.domain.exception.CrewNotFoundException;
import com.retrip.crew.domain.vo.CrewDescription;
import com.retrip.crew.domain.vo.CrewTitle;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import com.retrip.crew.infra.util.PaginationUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CrewService
        implements ManageCrewUseCase,
                UpdateRecruitmentUseCase,
                ManageDemandUseCase,
                GetCrewUseCase {
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewQueryRepository crewQueryRepository;

    @Override
    public CrewCreateResponse createCrew(CrewCreateRequest request) {
        Crew crew = crewRepository.save(request.to(request.leader()));
        return CrewCreateResponse.of(crew);
    }

    @Override
    public CrewUpdateResponse updateCrew(UUID crewId, CrewUpdateRequest request) {
        Crew crew = findById(crewId);
        CrewTitle title = new CrewTitle(request.title());
        CrewDescription description = new CrewDescription(request.description());
        Recruitment recruitment = crew.getRecruitment();

        crew.update(title, description);
        recruitment.updateMaxMembers(request.maxMembers());
        return CrewUpdateResponse.of(crew);
    }

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
    @Transactional(readOnly = true)
    public ScrollPageResponse<CrewListResponse> getCrews(
            Pageable pageable, String keyword, CrewOrder order, String sort) {
        Pageable orderPageable =
                PaginationUtils.createPageRequest(pageable, order.getField(), sort);
        Slice<CrewListResponse> result = crewQueryRepository.getCrews(orderPageable, keyword);
        Long totalCount = crewQueryRepository.getCrewCount(keyword);
        return ScrollPageResponse.of(totalCount, result.hasNext(), result.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public CrewDetailResponse getCrewDetail(UUID crewId) {
        Crew crew = findById(crewId);
        int memberCount = crewMemberRepository.countByCrewId(crewId);
        return CrewDetailResponse.of(crew, memberCount);
    }

    private Crew findById(UUID crewId) {
        return crewRepository.findById(crewId).orElseThrow(CrewNotFoundException::new);
    }
}
