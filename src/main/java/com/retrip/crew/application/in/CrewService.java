package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.request.CrewOrder;
import com.retrip.crew.application.in.response.CrewCreateResponse;
import com.retrip.crew.application.in.response.CrewDetailResponse;
import com.retrip.crew.application.in.response.CrewListResponse;
import com.retrip.crew.application.in.usecase.CreateCrewUseCase;
import com.retrip.crew.application.in.usecase.GetCrewUseCase;
import com.retrip.crew.application.out.repository.CrewQueryRepository;
import com.retrip.crew.application.out.repository.CrewRepository;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.exception.CrewNotFoundException;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import com.retrip.crew.infra.util.PaginationUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CrewService implements CreateCrewUseCase, GetCrewUseCase {
    private final CrewRepository crewRepository;
    private final CrewQueryRepository crewQueryRepository;

    @Override
    public CrewCreateResponse createCrew(CrewCreateRequest request) {
        Crew crew = crewRepository.save(request.to(request.leader()));

        return CrewCreateResponse.of(crew);
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
        Crew crew = findById(crewId);
        int memberCount = crewRepository.countById(crewId);
        return CrewDetailResponse.of(crew, memberCount);
    }

    private Crew findById(UUID crewId){
        return crewRepository.findById(crewId)
                .orElseThrow(CrewNotFoundException::new);
    }
}
