package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.IntroductionCreateRequest;
import com.retrip.crew.application.in.request.IntroductionDeleteRequest;
import com.retrip.crew.application.in.request.IntroductionUpdateRequest;
import com.retrip.crew.application.in.request.crew.CrewCreateRequest;
import com.retrip.crew.application.in.request.crew.CrewOrder;
import com.retrip.crew.application.in.request.crew.CrewUpdateRequest;
import com.retrip.crew.application.in.response.IntroductionCreateResponse;
import com.retrip.crew.application.in.response.IntroductionDetailResponse;
import com.retrip.crew.application.in.response.IntroductionListResponse;
import com.retrip.crew.application.in.response.IntroductionUpdateResponse;
import com.retrip.crew.application.in.response.crew.CrewCreateResponse;
import com.retrip.crew.application.in.response.crew.CrewDetailResponse;
import com.retrip.crew.application.in.response.crew.CrewListResponse;
import com.retrip.crew.application.in.response.crew.CrewUpdateResponse;
import com.retrip.crew.application.in.usecase.GetCrewUseCase;
import com.retrip.crew.application.in.usecase.ManageCrewUseCase;
import com.retrip.crew.application.in.usecase.ManageIntroductionUseCase;
import com.retrip.crew.application.out.repository.*;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Introduction;
import com.retrip.crew.domain.entity.Recruitment;
import com.retrip.crew.domain.exception.CrewNotFoundException;
import com.retrip.crew.domain.exception.IntroductionNotFoundException;
import com.retrip.crew.domain.vo.CrewDescription;
import com.retrip.crew.domain.vo.CrewTitle;
import com.retrip.crew.domain.vo.IntroductionContent;
import com.retrip.crew.domain.vo.IntroductionTitle;
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
public class CrewService implements ManageCrewUseCase, GetCrewUseCase, ManageIntroductionUseCase{
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewQueryRepository crewQueryRepository;
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
        return IntroductionCreateResponse.of(introduction);
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
